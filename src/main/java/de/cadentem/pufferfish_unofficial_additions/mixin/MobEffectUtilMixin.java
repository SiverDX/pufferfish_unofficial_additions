package de.cadentem.pufferfish_unofficial_additions.mixin;

import de.cadentem.pufferfish_unofficial_additions.rewards.EffectReward;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectUtil.class)
public abstract class MobEffectUtilMixin {
    @Inject(method = "formatDuration", at = @At("HEAD"), cancellable = true)
    private static void pufferfish_unofficial_additions$handleInfiniteDuration(final MobEffectInstance instance, final float durationFactor, final CallbackInfoReturnable<String> callback) {
        if (instance.getDuration() == EffectReward.INFINITE_DURATION) {
            // 1.20 also does not have a translation for this
            callback.setReturnValue("infinite");
        }
    }
}
