package de.cadentem.pufferfish_unofficial_additions.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.cadentem.pufferfish_unofficial_additions.rewards.EffectReward;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @WrapOperation(method = "renderEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;getDuration()I", ordinal = 0))
    private int pufferfish_unofficial_additions$handleInfiniteDuration(final MobEffectInstance instance, final Operation<Integer> original) {
        int duration = original.call(instance);

        if (duration == EffectReward.INFINITE_DURATION) {
            return Integer.MAX_VALUE;
        }

        return duration;
    }
}
