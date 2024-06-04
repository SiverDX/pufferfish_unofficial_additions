package de.cadentem.pufferfish_unofficial_additions.events;

import de.cadentem.pufferfish_unofficial_additions.experience.FishingExperienceSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.puffish.skillsmod.api.SkillsAPI;

@Mod.EventBusSubscriber
public class ForgeEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void grantFishingExperience(final ItemFishedEvent event) {
        if (event.isCanceled()) {
            return;
        }

        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            if (event.getDrops().isEmpty()) {
               SkillsAPI.updateExperienceSources(serverPlayer, FishingExperienceSource.class, source -> source.getValue(serverPlayer, serverPlayer.getMainHandItem(), ItemStack.EMPTY));
            } else {
                event.getDrops().forEach(drop -> SkillsAPI.updateExperienceSources(serverPlayer, FishingExperienceSource.class, source -> source.getValue(serverPlayer, serverPlayer.getMainHandItem(), drop)));
            }
        }
    }
}
