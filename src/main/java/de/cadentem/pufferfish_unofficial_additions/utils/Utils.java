package de.cadentem.pufferfish_unofficial_additions.utils;

import de.cadentem.pufferfish_unofficial_additions.PUA;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class Utils {
    @Deprecated(forRemoval = true)
    public static ResourceLocation fixAttribute(final @Nullable ResourceLocation location) {
        if (location != null && location.getNamespace().equals(PUA.MODID)) {
            return new ResourceLocation("additional_attributes", location.getPath());
        }

        return location;
    }
}
