package de.cadentem.pufferfish_unofficial_additions.mixin;

import de.cadentem.pufferfish_unofficial_additions.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @ModifyArg(method = "getAttributeModifiers", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;getOptional(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;"))
    private ResourceLocation pufferfish_unofficial_additions$fixAttribute(final ResourceLocation location) {
        return Utils.fixAttribute(location);
    }
}
