package de.cadentem.pufferfish_unofficial_additions.mixin.irons_spellbooks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.cadentem.pufferfish_unofficial_additions.irons_spellbooks.Attributes;
import io.redspace.ironsspellbooks.item.Scroll;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Scroll.class)
public class MixinScroll {
    /** Grants the chance to not consume the used scroll (skill) */
    @WrapOperation(method = "removeScrollAfterCast", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isCreative()Z"))
    private boolean pufferfish_unofficial_additions$considerSkill(final ServerPlayer instance, final Operation<Boolean> original) {
        if (instance.isCreative()) {
            return true;
        }

        AttributeInstance attributeInstance = instance.getAttribute(Attributes.KEEP_SCROLL.get());

        if (attributeInstance != null) {
            double value = attributeInstance.getValue();

            if (value == 0) {
                return false;
            }

            if (attributeInstance.getValue() >= instance.getRandom().nextFloat()) {
                return true;
            }
        }

        return original.call(instance);
    }
}
