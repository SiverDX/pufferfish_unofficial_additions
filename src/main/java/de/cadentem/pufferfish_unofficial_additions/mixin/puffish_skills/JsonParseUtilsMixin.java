package de.cadentem.pufferfish_unofficial_additions.mixin.puffish_skills;

import de.cadentem.pufferfish_unofficial_additions.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.puffish.skillsmod.api.utils.JsonParseUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(JsonParseUtils.class)
public abstract class JsonParseUtilsMixin {
    @ModifyArg(method = "lambda$parseAttribute$10", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;getOptional(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;"))
    private static ResourceLocation pufferfish_unofficial_additions$fixAttribute(final ResourceLocation location) {
        return Utils.fixAttribute(location);
    }
}
