package de.cadentem.pufferfish_unofficial_additions.registry;

import com.mojang.serialization.Codec;
import de.cadentem.pufferfish_unofficial_additions.PUA;
import de.cadentem.pufferfish_unofficial_additions.loot_modifiers.HarvestLootAmplifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PUALootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, PUA.MODID);

    public static final RegistryObject<Codec<HarvestLootAmplifier>> HARVEST_LOOT_AMPLIFIER = LOOT_MODIFIERS.register(HarvestLootAmplifier.ID, () -> HarvestLootAmplifier.CODEC);
}
