package de.cadentem.pufferfish_unofficial_additions.mixin;

import de.cadentem.pufferfish_unofficial_additions.rewards.EffectReward;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Events don't provide a proper way to modify the instance (this way the immune check also happens after modifications) */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin{
    @ModifyVariable(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), argsOnly = true)
    private MobEffectInstance modifyEffect(final MobEffectInstance instance) {
        if ((Object) this instanceof ServerPlayer player) {
            return EffectReward.modifyEffect(player, instance);
        }

        return instance;
    }
}
