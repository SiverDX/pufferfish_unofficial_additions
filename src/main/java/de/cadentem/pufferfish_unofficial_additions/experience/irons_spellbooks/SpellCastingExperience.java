package de.cadentem.pufferfish_unofficial_additions.experience.irons_spellbooks;

import de.cadentem.pufferfish_unofficial_additions.PUA;
import de.cadentem.pufferfish_unofficial_additions.conditions.StringCondition;
import io.redspace.ironsspellbooks.api.item.IScroll;
import io.redspace.ironsspellbooks.api.item.ISpellbook;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
            Map.entry("item", ItemCondition.factory().map(c -> c.map(SpellCastingExperience.Context::castStack))),
            Map.entry("item_nbt", ItemNbtCondition.factory().map(c -> c.map(SpellCastingExperience.Context::castStack))),
            Map.entry("item_tag", ItemTagCondition.factory().map(c -> c.map(SpellCastingExperience.Context::castStack))),
            Map.entry("school_type", StringCondition.factory().map(c -> c.map(SpellCastingExperience.Context::schoolType))),
            Map.entry("spell_id", StringCondition.factory().map(c -> c.map(SpellCastingExperience.Context::spellId)))
    );

    private static final Map<String, ParameterFactory<SpellCastingExperience.Context>> PARAMETERS = Map.ofEntries(
            Map.entry("level", ParameterFactory.simple(SpellCastingExperience.Context::level)),
            Map.entry("min_level", ParameterFactory.simple(SpellCastingExperience.Context::minLevel)),
            Map.entry("max_level", ParameterFactory.simple(SpellCastingExperience.Context::maxLevel)),
            Map.entry("rarity", ParameterFactory.simple(SpellCastingExperience.Context::rarity)),
            Map.entry("mana_cost", ParameterFactory.simple(SpellCastingExperience.Context::manaCost))
    );

    private final CalculationManager<SpellCastingExperience.Context> manager;

    private SpellCastingExperience(final CalculationManager<SpellCastingExperience.Context> calculated) {
        this.manager = calculated;
    }

    public static void register() {
        SkillsAPI.registerExperienceSourceWithData(ID, (json, context) -> json.getAsObject().andThen(rootObject -> create(rootObject, context)));
    }

    private static Result<SpellCastingExperience, Failure> create(final JsonObjectWrapper rootObject, final ConfigContext context) {
        return CalculationManager.create(rootObject, CONDITIONS, PARAMETERS, context).mapSuccess(SpellCastingExperience::new);
    }

    public int getValue(final ServerPlayer caster, final SchoolType schoolType, final String spellId, int level) {
        ItemStack castStack = caster.getMainHandItem();
        ItemStack offhandStack = caster.getOffhandItem();

        if (!isSpellItem(castStack) && isSpellItem(offhandStack)) {
            castStack = offhandStack;
        }

        AbstractSpell spell = SpellRegistry.getSpell(spellId);

        if (spell == null) {
            PUA.LOG.warn("Spell [{}] could not be found", spellId);
            return 0;
        }

        return this.manager.getValue(new SpellCastingExperience.Context(caster, castStack, schoolType.getId().toString(), spellId, level, spell.getMinLevel(), spell.getMaxLevel(), spell.getRarity(level).ordinal(), spell.getManaCost(level, caster)));
    }


    private boolean isSpellItem(final ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item instanceof ISpellbook || item instanceof IScroll;
    }

    @Override
    public void dispose(final MinecraftServer minecraftServer) { /* Nothing to do */ }

    private record Context(ServerPlayer caster, ItemStack castStack, String schoolType, String spellId, double level, double minLevel, double maxLevel, double rarity, double manaCost) {
        public double level() {
            return level;
        }

        public double minLevel() {
            return minLevel;
        }

        public double maxLevel() {
            return maxLevel;
        }

        public double rarity() {
            return rarity;
        }

        public double manaCost() {
            return manaCost;
        }
    }
}