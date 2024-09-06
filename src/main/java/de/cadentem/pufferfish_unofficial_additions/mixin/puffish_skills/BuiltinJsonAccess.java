package de.cadentem.pufferfish_unofficial_additions.mixin.puffish_skills;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.puffish.skillsmod.api.json.BuiltinJson;
import net.puffish.skillsmod.api.json.JsonElement;
import net.puffish.skillsmod.api.util.Problem;
import net.puffish.skillsmod.api.util.Result;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(BuiltinJson.class)
public interface BuiltinJsonAccess {
    @Invoker("parseSomethingOrSomethingTag")
    static <T> Result<HolderSet<T>, Problem> pufferfish_unofficial_additions$parseSomethingOrSomethingTag(final JsonElement element, final Registry<T> registry, final Supplier<String> expected, final Function<ResourceLocation, String> unknown) {
        throw new AssertionError();
    }
}
