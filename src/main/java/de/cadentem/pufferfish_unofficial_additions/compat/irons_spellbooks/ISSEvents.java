package de.cadentem.pufferfish_unofficial_additions.compat.irons_spellbooks;

import de.cadentem.pufferfish_unofficial_additions.PUA;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.puffish.skillsmod.api.SkillsAPI;

public class ISSEvents {
    public static void grantSpellExperience(final SpellOnCastEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) {
            return;
        }

        ItemStack mainHand = serverPlayer.getMainHandItem();
        ItemStack spellbook = Utils.getPlayerSpellbookStack(serverPlayer);

        if (spellbook == null) {
            spellbook = ItemStack.EMPTY;
        }

        AbstractSpell spell = SpellRegistry.getSpell(event.getSpellId());

        if (spell == null) {
            PUA.LOG.warn("Spell [{}] could not be found", event.getSpellId());
            return;
        }

        SpellRarity rarity = spell.getRarity(event.getSpellLevel());

        int minLevelRarity = spell.getMinLevelForRarity(rarity);
        int manaCost = event.getCastSource().consumesMana() ? spell.getManaCost(event.getSpellLevel()) : 0;
        int manaCostPerSecond = event.getCastSource().consumesMana() ? spell.getCastType() == CastType.CONTINUOUS ? manaCost * (20 / MagicManager.CONTINUOUS_CAST_TICK_INTERVAL) : 0 : 0;
        int castDurationTicks = spell.getCastType() == CastType.CONTINUOUS ? spell.getEffectiveCastTime(event.getSpellLevel(), serverPlayer) : 0;
        double castDuration = castDurationTicks / 20d;
        double castChargeTime = spell.getCastType() == CastType.LONG ? spell.getEffectiveCastTime(event.getSpellLevel() ,serverPlayer) / 20d : 0;
        double cooldown = MagicManager.getEffectiveSpellCooldown(spell, serverPlayer, event.getCastSource()) / 20d;
        int expectedTicks = spell.getCastType() == CastType.CONTINUOUS ? (castDurationTicks / 10) : 1;

        Data data = new Data(serverPlayer, mainHand, spellbook, event.getSchoolType(), spell, rarity, event.getSpellLevel(), minLevelRarity, manaCost, manaCostPerSecond, castDuration, castChargeTime, cooldown, expectedTicks);
        PUA.LOG.debug("Spell experience source data: [{}]", data);

        SkillsAPI.updateExperienceSources(serverPlayer, SpellCastingExperienceSource.class, source -> source.getValue(data));
    }
}
