package de.cadentem.pufferfish_unofficial_additions.events;

import de.cadentem.pufferfish_unofficial_additions.experience.FishingExperienceSource;
import de.cadentem.pufferfish_unofficial_additions.registry.PUAAttributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.puffish.skillsmod.SkillsMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void grantFishingExperience(final ItemFishedEvent event) {
        if (event.isCanceled()) {
            return;
        }

        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            event.getDrops().forEach(drop -> SkillsMod.getInstance().visitExperienceSources(serverPlayer, experienceSource ->
                    experienceSource instanceof FishingExperienceSource fishingExperienceSource ? fishingExperienceSource.getValue(serverPlayer, serverPlayer.getMainHandItem(), drop) : 0
            ));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void amplyLootingLevel(final LootingLevelEvent event) {
        event.setLootingLevel(PUAAttributes.getIntValue(event.getEntity(), PUAAttributes.LOOTING.get(), event.getLootingLevel()));
    }
}
