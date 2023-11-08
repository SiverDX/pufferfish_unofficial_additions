package de.cadentem.pufferfish_unofficial_additions.mixin;

import de.cadentem.pufferfish_unofficial_additions.registry.PUAAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @ModifyVariable(method = "decreaseAirSupply", at = @At(value = "STORE"), ordinal = 1)
    private int pufferfish_unofficial_additions$amplifyValue(int original) {
        LivingEntity instance = (LivingEntity) (Object) this;

        if (instance instanceof Player player) {
            return PUAAttributes.getIntValue(player, PUAAttributes.RESPIRATION.get(), original);
        }

        return original;
    }
}
