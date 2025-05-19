package de.cadentem.pufferfish_unofficial_additions.compat.irons_spellbooks;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record Data(
        ServerPlayer caster,
        ItemStack mainHand,
        ItemStack spellbook,
        SchoolType school,
        AbstractSpell spell,
        SpellRarity rarity,
        double level,
        double minLevelRarity,
        double manaCost,
        double manaCostPerSecond,
        double castDuration,
        double castChargeTime,
        double cooldown,
        double expectedTicks
) {
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