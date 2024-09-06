package de.cadentem.pufferfish_unofficial_additions.compat.irons_spellbooks;

import de.cadentem.pufferfish_unofficial_additions.PUA;
import de.cadentem.pufferfish_unofficial_additions.prototypes.CustomPrototypes;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.puffish.skillsmod.api.SkillsAPI;
import net.puffish.skillsmod.api.calculation.Calculation;
import net.puffish.skillsmod.api.calculation.operation.OperationFactory;
import net.puffish.skillsmod.api.calculation.prototype.BuiltinPrototypes;
import net.puffish.skillsmod.api.calculation.prototype.Prototype;
import net.puffish.skillsmod.api.experience.source.ExperienceSource;
import net.puffish.skillsmod.api.experience.source.ExperienceSourceConfigContext;
import net.puffish.skillsmod.api.experience.source.ExperienceSourceDisposeContext;
import net.puffish.skillsmod.api.util.Problem;
import net.puffish.skillsmod.api.util.Result;
import net.puffish.skillsmod.calculation.LegacyCalculation;

public class SpellCastingExperienceSource implements ExperienceSource {
    private static final ResourceLocation ID = PUA.location("spell_casting");
    private static final Prototype<Data> PROTOTYPE = Prototype.create(ID);

    static {
        PROTOTYPE.registerOperation(PUA.location("player"), BuiltinPrototypes.PLAYER, OperationFactory.create(Data::caster));
        PROTOTYPE.registerOperation(PUA.location("main_hand"), BuiltinPrototypes.ITEM_STACK, OperationFactory.create(Data::mainHand));
        PROTOTYPE.registerOperation(PUA.location("spellbook"), BuiltinPrototypes.ITEM_STACK, OperationFactory.create(Data::spellbook));
        PROTOTYPE.registerOperation(PUA.location("school"), ISPrototypes.SCHOOL, OperationFactory.create(Data::school));
        PROTOTYPE.registerOperation(PUA.location("spell"), ISPrototypes.SPELL, OperationFactory.create(Data::spell));

        PROTOTYPE.registerOperation(PUA.location("level"), BuiltinPrototypes.NUMBER, OperationFactory.create(Data::level));
        PROTOTYPE.registerOperation(PUA.location("min_level_rarity"), BuiltinPrototypes.NUMBER, OperationFactory.create(data -> (double) data.spell.getMinLevelForRarity(data.rarity)));

        PROTOTYPE.registerOperation(PUA.location("rarity_name"), CustomPrototypes.STRING, OperationFactory.create(data -> data.rarity.name()));
        PROTOTYPE.registerOperation(PUA.location("rarity"), BuiltinPrototypes.NUMBER, OperationFactory.create(data -> (double) data.rarity.ordinal()));

        PROTOTYPE.registerOperation(PUA.location("mana_cost"), BuiltinPrototypes.NUMBER, OperationFactory.create(Data::manaCost));
        PROTOTYPE.registerOperation(PUA.location("mana_cost_per_second"), BuiltinPrototypes.NUMBER, OperationFactory.create(Data::manaCostPerSecond));
        PROTOTYPE.registerOperation(PUA.location("cast_duration"), BuiltinPrototypes.NUMBER, OperationFactory.create(Data::castDuration));
        PROTOTYPE.registerOperation(PUA.location("cast_charge_time"), BuiltinPrototypes.NUMBER, OperationFactory.create(Data::castChargeTime));
        PROTOTYPE.registerOperation(PUA.location("cooldown"), BuiltinPrototypes.NUMBER, OperationFactory.create(Data::cooldown));
        PROTOTYPE.registerOperation(PUA.location("expected_ticks"), BuiltinPrototypes.NUMBER, OperationFactory.create(Data::expectedTicks));
    }

    private final Calculation<Data> calculation;

    private SpellCastingExperienceSource(final Calculation<Data> calculation) {
        this.calculation = calculation;
    }

    public static void register() {
        SkillsAPI.registerExperienceSource(ID, SpellCastingExperienceSource::parse);
    }

    private static Result<SpellCastingExperienceSource, Problem> parse(final ExperienceSourceConfigContext context) {
        return context.getData().andThen((rootElement -> LegacyCalculation.parse(rootElement, PROTOTYPE, context).mapSuccess(SpellCastingExperienceSource::new)));
    }

    private record Data(ServerPlayer caster, ItemStack mainHand, ItemStack spellbook, SchoolType school, AbstractSpell spell, SpellRarity rarity, double level, double minLevelRarity, double manaCost, double manaCostPerSecond, double castDuration, double castChargeTime, double cooldown, double expectedTicks) {
        @Override
        public String toString() {
            return "Data{" +
                    "caster=" + caster +
                    ", mainHand=" + mainHand +
                    ", spellbook=" + spellbook +
                    ", school=" + school.getId() +
                    ", spell=" + spell.getSpellResource() +
                    ", rarity=" + rarity +
                    ", level=" + level +
                    ", minLevelRarity=" + minLevelRarity +
                    ", manaCost=" + manaCost +
                    ", manaCostPerSecond=" + manaCostPerSecond +
                    ", castDuration=" + castDuration +
                    ", castChargeTime=" + castChargeTime +
                    ", cooldown=" + cooldown +
                    ", expectedTicks=" + expectedTicks +
                    '}';
        }
    }

    public int getValue(final ServerPlayer caster, final SchoolType school, final String spellId, int level, final CastSource castSource) {
        ItemStack mainHand = caster.getMainHandItem();
        ItemStack spellbook = Utils.getPlayerSpellbookStack(caster);

        if (spellbook == null) {
            spellbook = ItemStack.EMPTY;
        }

        AbstractSpell spell = SpellRegistry.getSpell(spellId);

        if (spell == null) {
            PUA.LOG.warn("Spell [{}] could not be found", spellId);
            return 0;
        }

        SpellRarity rarity = spell.getRarity(level);

        int minLevelRarity = spell.getMinLevelForRarity(rarity);
        int manaCost = castSource.consumesMana() ? spell.getManaCost(level) : 0;
        int manaCostPerSecond = castSource.consumesMana() ? spell.getCastType() == CastType.CONTINUOUS ? manaCost * (20 / MagicManager.CONTINUOUS_CAST_TICK_INTERVAL) : 0 : 0;
        int castDurationTicks = spell.getCastType() == CastType.CONTINUOUS ? spell.getEffectiveCastTime(level, caster) : 0;
        double castDuration = castDurationTicks / 20d;
        double castChargeTime = spell.getCastType() == CastType.LONG ? spell.getEffectiveCastTime(level ,caster) / 20d : 0;
        double cooldown = MagicManager.getEffectiveSpellCooldown(spell, caster, castSource) / 20d;
        int expectedTicks = spell.getCastType() == CastType.CONTINUOUS ? (castDurationTicks / 10) : 1;

        Data data = new Data(caster, mainHand, spellbook, school, spell, rarity, level, minLevelRarity, manaCost, manaCostPerSecond, castDuration, castChargeTime, cooldown, expectedTicks);
        PUA.LOG.debug("Spell experience source data: [{}]", data);

        return (int) Math.round(calculation.evaluate(data));
    }

    @Override
    public void dispose(final ExperienceSourceDisposeContext experienceSourceDisposeContext) { /* Nothing to do */ }
}