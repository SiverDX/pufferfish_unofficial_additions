package de.cadentem.pufferfish_unofficial_additions.datagen;

import de.cadentem.pufferfish_unofficial_additions.PUA;
import de.cadentem.pufferfish_unofficial_additions.loot_modifiers.HarvestLootAmplifier;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class PUALootModifiers extends GlobalLootModifierProvider {
    public PUALootModifiers(final PackOutput output) {
        super(output, PUA.MODID);
    }

    @Override
    protected void start() {
        add(HarvestLootAmplifier.ID, new HarvestLootAmplifier(new LootItemCondition[]{}));
    }
}
