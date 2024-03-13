package de.cadentem.pufferfish_unofficial_additions.mixin.puffish_skills;

import de.cadentem.pufferfish_unofficial_additions.PUA;
import net.minecraft.resources.ResourceLocation;
import net.puffish.skillsmod.api.utils.JsonParseUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(JsonParseUtils.class)
public class JsonParseUtilsMixin {
    @ModifyArg(method = "lambda$parseAttribute$12", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;getOptional(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;"), remap = false)
    private static ResourceLocation modifyNamespace(final ResourceLocation location) {
        if (location != null && location.getNamespace().equals(PUA.MODID)) {
            return new ResourceLocation("additional_attributes", location.getPath());
        }

        return location;
    }
}
