package de.cadentem.pufferfish_unofficial_additions;

import com.mojang.logging.LogUtils;
import de.cadentem.pufferfish_unofficial_additions.experience.FishingExperienceSource;
import de.cadentem.pufferfish_unofficial_additions.experience.HarvestExperienceSource;
import de.cadentem.pufferfish_unofficial_additions.experience.irons_spellbooks.SpellCastingExperience;
import de.cadentem.pufferfish_unofficial_additions.irons_spellbooks.ISAttributes;
import de.cadentem.pufferfish_unofficial_additions.irons_spellbooks.ISEvents;
import de.cadentem.pufferfish_unofficial_additions.registry.PUAAttributes;
import de.cadentem.pufferfish_unofficial_additions.registry.PUALootModifiers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
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
            ISAttributes.ATTRIBUTES.register(modEventBus);
            SpellCastingExperience.register();
            modEventBus.addListener(ISAttributes::setAttributes);
            MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, ISEvents::handleSpellCast);
        }
    }
}
