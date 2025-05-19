package de.cadentem.pufferfish_unofficial_additions.events;

import de.cadentem.pufferfish_unofficial_additions.experience.FishingExperienceSource;
import de.cadentem.pufferfish_unofficial_additions.rewards.EffectReward;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.puffish.skillsmod.api.SkillsAPI;

@Mod.EventBusSubscriber
public class ForgeEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void grantFishingExperience(final ItemFishedEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            if (event.getDrops().isEmpty()) {
               SkillsAPI.updateExperienceSources(serverPlayer, FishingExperienceSource.class, source -> source.getValue(serverPlayer, serverPlayer.getMainHandItem(), ItemStack.EMPTY));
            } else {
                event.getDrops().forEach(drop -> SkillsAPI.updateExperienceSources(serverPlayer, FishingExperienceSource.class, source -> source.getValue(serverPlayer, serverPlayer.getMainHandItem(), drop)));
            }
        }
    }

    @SubscribeEvent
    public static void immuneEffects(final MobEffectEvent.Applicable event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            MobEffect effect = event.getEffectInstance().getEffect();
            int amplifier = event.getEffectInstance().getAmplifier();

            if (EffectReward.isImmune(player.getUUID(), effect, amplifier)) {
                event.setResult(Event.Result.DENY);
            }
        }
    }

    /** Avoid loss of effects on clear command */
    @SubscribeEvent
    public static void retainEffects(final MobEffectEvent.Remove event) {
        MobEffectInstance instance = event.getEffectInstance();

        if (event.getEntity() instanceof ServerPlayer player && instance != null && instance.getDuration() == -1) {
            MobEffect effect = instance.getEffect();
            int amplifier = instance.getAmplifier();

            if (EffectReward.shouldRemove(player.getUUID(), effect, amplifier)) {
                event.setCanceled(true);
            }
        }
    }

    /** Avoid loss of effects on death / when changing dimensions */
    @SubscribeEvent
    public static void retainEffects(final EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            EffectReward.applyEffects(player);
        }
    }

    @SubscribeEvent
    public static void clearData(final PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            EffectReward.clearData(player.getUUID());
        }
    }
}
