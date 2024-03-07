package de.cadentem.pufferfish_unofficial_additions.experience.irons_spellbooks;

import de.cadentem.pufferfish_unofficial_additions.PUA;
import de.cadentem.pufferfish_unofficial_additions.conditions.StringCondition;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.puffish.skillsmod.api.SkillsAPI;
import net.puffish.skillsmod.api.config.ConfigContext;
import net.puffish.skillsmod.api.experience.ExperienceSource;
import net.puffish.skillsmod.api.experience.calculation.condition.ConditionFactory;
import net.puffish.skillsmod.api.experience.calculation.condition.ItemCondition;
import net.puffish.skillsmod.api.experience.calculation.condition.ItemNbtCondition;
import net.puffish.skillsmod.api.experience.calculation.condition.ItemTagCondition;
import net.puffish.skillsmod.api.experience.calculation.parameter.ParameterFactory;
import net.puffish.skillsmod.api.json.JsonObjectWrapper;
import net.puffish.skillsmod.api.utils.Result;
import net.puffish.skillsmod.api.utils.failure.Failure;
import net.puffish.skillsmod.experience.calculation.CalculationManager;

import java.util.Map;

public class SpellCastingExperience implements ExperienceSource {
    public static final ResourceLocation ID = new ResourceLocation(PUA.MODID, "spell_casting");

    private static final Map<String, ConditionFactory<SpellCastingExperience.Context>> CONDITIONS = Map.ofEntries(
            Map.entry("item", ItemCondition.factory().map(c -> c.map(SpellCastingExperience.Context::mainHand))),
            Map.entry("item_nbt", ItemNbtCondition.factory().map(c -> c.map(SpellCastingExperience.Context::mainHand))),
            Map.entry("item_tag", ItemTagCondition.factory().map(c -> c.map(SpellCastingExperience.Context::mainHand))),
            Map.entry("spellbook", ItemCondition.factory().map(c -> c.map(SpellCastingExperience.Context::spellbook))),
            Map.entry("spellbook_nbt", ItemNbtCondition.factory().map(c -> c.map(SpellCastingExperience.Context::spellbook))),
            Map.entry("spellbook_tag", ItemTagCondition.factory().map(c -> c.map(SpellCastingExperience.Context::spellbook))),
            Map.entry("school_type", StringCondition.factory().map(c -> c.map(SpellCastingExperience.Context::schoolType))),
            Map.entry("spell_id", StringCondition.factory().map(c -> c.map(SpellCastingExperience.Context::spellId))),
            Map.entry("cast_type", StringCondition.factory().map(c -> c.map(SpellCastingExperience.Context::castType))),
            Map.entry("spell_rarity_name", StringCondition.factory().map(c -> c.map(SpellCastingExperience.Context::rarityName)))
    );

    private static final Map<String, ParameterFactory<SpellCastingExperience.Context>> PARAMETERS = Map.ofEntries(
            Map.entry("level", ParameterFactory.simple(SpellCastingExperience.Context::level)),
            Map.entry("min_level", ParameterFactory.simple(SpellCastingExperience.Context::minLevel)),
            Map.entry("min_level_rarity", ParameterFactory.simple(SpellCastingExperience.Context::minLevelRarity)),
            Map.entry("max_level", ParameterFactory.simple(SpellCastingExperience.Context::maxLevel)),
            Map.entry("rarity", ParameterFactory.simple(SpellCastingExperience.Context::rarity)),
            Map.entry("mana_cost", ParameterFactory.simple(SpellCastingExperience.Context::manaCost)),
            Map.entry("mana_cost_per_second", ParameterFactory.simple(SpellCastingExperience.Context::manaCostPerSecond)),
            Map.entry("cast_duration", ParameterFactory.simple(SpellCastingExperience.Context::castDuration)),
            Map.entry("cast_charge_time", ParameterFactory.simple(SpellCastingExperience.Context::castChargeTime)),
            Map.entry("cooldown", ParameterFactory.simple(SpellCastingExperience.Context::cooldown)),
            Map.entry("expected_ticks", ParameterFactory.simple(SpellCastingExperience.Context::expectedTicks))
    );

    private final CalculationManager<SpellCastingExperience.Context> manager;

    private SpellCastingExperience(final CalculationManager<SpellCastingExperience.Context> calculated) {
        this.manager = calculated;
    }

    public static void register() {
        SkillsAPI.registerExperienceSourceWithData(ID, (json, context) -> json.getAsObject().andThen(rootObject -> create(rootObject, context)));
    }

    private static Result<SpellCastingExperience, Failure> create(final JsonObjectWrapper root, final ConfigContext context) {
        return CalculationManager.create(root, CONDITIONS, PARAMETERS, context).mapSuccess(SpellCastingExperience::new);
    }

    public int getValue(final ServerPlayer caster, final SchoolType schoolType, final String spellId, int level, final CastSource castSource) {
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

        String school = schoolType.getId().toString();
        SpellRarity spellRarity = spell.getRarity(level);

        int minLevel = spell.getMinLevel();
        int minLevelRarity = spell.getMinLevelForRarity(spellRarity);
        int maxLevel = spell.getMaxLevel();
        int rarity = spellRarity.ordinal();
        int manaCost = castSource.consumesMana() ? spell.getManaCost(level, caster) : 0;
        int manaCostPerSecond = castSource.consumesMana() ? spell.getCastType() == CastType.CONTINUOUS ? manaCost * (20 / MagicManager.CONTINUOUS_CAST_TICK_INTERVAL) : 0 : 0;
        int castDurationTicks = spell.getCastType() == CastType.CONTINUOUS ? spell.getEffectiveCastTime(level, caster) : 0;
        double castDuration = castDurationTicks / 20d;
        double castChargeTime = spell.getCastType() == CastType.LONG ? spell.getEffectiveCastTime(level ,caster) / 20d : 0;
        double cooldown = MagicManager.getEffectiveSpellCooldown(spell, caster, castSource) / 20d;
        int expectedTicks = spell.getCastType() == CastType.CONTINUOUS ? (castDurationTicks / 10) : 1;

        int experienceGained = this.manager.getValue(new Context(caster, mainHand, spellbook, school, spellId, spell.getCastType().name(), level, minLevel, minLevelRarity, maxLevel, spellRarity.name(), rarity, manaCost, manaCostPerSecond, castDuration, castChargeTime, cooldown, expectedTicks));

        PUA.LOG.debug("""
                Context of [{}]:
                - Caster: [{}]
                - Main Hand: [{}]
                - Spellbook: [{}]
                - School: [{}]
                - Spell: [{}]
                - Cast type: [{}]
                - Level: [{} | min: {} (for rarity: {}) | max: {}]
                - Rarity: [{}] (ordinal: {})
                - Mana cost: [{}]
                - Mana cost per second: [{}]
                - Cast duration: [{}]
                - Cast charge time: [{}]
                - Cooldown: [{}]
                - Expected ticks: [{}]
                - Experience gained: [{}]
                """, ID, caster, ForgeRegistries.ITEMS.getKey(mainHand.getItem()), ForgeRegistries.ITEMS.getKey(spellbook.getItem()), school, spellId, spell.getCastType().name(), level, minLevel, minLevelRarity, maxLevel, spellRarity.name(), rarity, manaCost, manaCostPerSecond, castDuration, castChargeTime, cooldown, expectedTicks, experienceGained);

        return experienceGained;
    }

    @Override
    public void dispose(final MinecraftServer server) { /* Nothing to do */ }

    private record Context(ServerPlayer caster, ItemStack mainHand, ItemStack spellbook, String schoolType, String spellId, String castType, double level, double minLevel, double minLevelRarity, double maxLevel, String rarityName, double rarity, double manaCost, double manaCostPerSecond, double castDuration, double castChargeTime, double cooldown, double expectedTicks) { }
}