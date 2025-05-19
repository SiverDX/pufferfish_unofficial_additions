package de.cadentem.pufferfish_unofficial_additions.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.cadentem.pufferfish_unofficial_additions.misc.ModificationHandler;
import de.cadentem.pufferfish_unofficial_additions.rewards.EffectReward;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin implements ModificationHandler {
    @Shadow int duration;

    @Unique private static final String pufferfish_unofficial_additions$TAG = "pufferfish_unofficial_additions.modified";
    @Unique private boolean pufferfish_unofficial_additions$modified;

    @Override
    public void pufferfish_unofficial_additions$setModified(boolean modified) {
        this.pufferfish_unofficial_additions$modified = modified;
    }

    @Override
    public boolean pufferfish_unofficial_additions$wasModified() {
        return pufferfish_unofficial_additions$modified;
    }

    @Inject(method = "save", at = @At("TAIL"))
    private void pufferfish_unofficial_additions$saveModified(final CompoundTag tag, final CallbackInfoReturnable<CompoundTag> callback) {
        tag.putBoolean(pufferfish_unofficial_additions$TAG, pufferfish_unofficial_additions$modified);
    }

    @Inject(method = "load", at = @At("TAIL"))
    private static void pufferfish_unofficial_additions$loadModified(final CompoundTag nbt, final CallbackInfoReturnable<MobEffectInstance> callback) {
        ((ModificationHandler) callback.getReturnValue()).pufferfish_unofficial_additions$setModified(nbt.getBoolean(pufferfish_unofficial_additions$TAG));
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;isDurationEffectTick(II)Z"), index = 0)
    private int pufferfish_unofficial_additions$tickWithInfinite(int duration, @Local(argsOnly = true) final LivingEntity entity) {
        if (duration == EffectReward.INFINITE_DURATION) {
            return entity.tickCount;
        }

        return duration;
    }

    @ModifyReturnValue(method = "tick", at = @At("RETURN"))
    private boolean pufferfish_unofficial_additions$handleInfiniteDuration(final boolean hasDuration) {
        return hasDuration || duration == EffectReward.INFINITE_DURATION;
    }

    @Inject(method = "tickDownDuration", at = @At(value = "FIELD", target = "Lnet/minecraft/world/effect/MobEffectInstance;duration:I", ordinal = 0), cancellable = true)
    private void pufferfish_unofficial_additions$handleInfiniteDuration(final CallbackInfoReturnable<Integer> callback) {
        if (duration == EffectReward.INFINITE_DURATION) {
            callback.setReturnValue(duration);
        }
    }

    @ModifyExpressionValue(method = "update", at = {
            @At(value = "FIELD", target = "Lnet/minecraft/world/effect/MobEffectInstance;duration:I", ordinal = 1),
            @At(value = "FIELD", target = "Lnet/minecraft/world/effect/MobEffectInstance;duration:I", ordinal = 2),
            @At(value = "FIELD", target = "Lnet/minecraft/world/effect/MobEffectInstance;duration:I", ordinal = 5),
            @At(value = "FIELD", target = "Lnet/minecraft/world/effect/MobEffectInstance;duration:I", ordinal = 6)
    })
    private int pufferfish_unofficial_additions$handleInfiniteDuration(final int duration) {
        if (duration == EffectReward.INFINITE_DURATION) {
            return Integer.MAX_VALUE;
        }

        return duration;
    }
}
