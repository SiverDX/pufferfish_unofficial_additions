package de.cadentem.pufferfish_unofficial_additions;

import com.mojang.logging.LogUtils;
import de.cadentem.pufferfish_unofficial_additions.experience.FishingExperienceSource;
import de.cadentem.pufferfish_unofficial_additions.experience.HarvestExperienceSource;
import de.cadentem.pufferfish_unofficial_additions.irons_spellbooks.Attributes;
import de.cadentem.pufferfish_unofficial_additions.loot_modifiers.HarvestLootAmplifier;
import de.cadentem.pufferfish_unofficial_additions.registry.PUAAttributes;
import de.cadentem.pufferfish_unofficial_additions.registry.PUALootModifiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(PUA.MODID)
public class PUA {
    public static final String MODID = "pufferfish_unofficial_additions";
    public static final Logger LOG = LogUtils.getLogger();

    public PUA() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        HarvestExperienceSource.register();
        FishingExperienceSource.register();

        PUALootModifiers.LOOT_MODIFIERS.register(modEventBus);
        PUAAttributes.ATTRIBUTES.register(modEventBus);

        if (ModList.get().isLoaded("irons_spellbooks")) {
            Attributes.ATTRIBUTES.register(modEventBus);
            modEventBus.addListener(Attributes::setAttributes);
        }
    }
}
