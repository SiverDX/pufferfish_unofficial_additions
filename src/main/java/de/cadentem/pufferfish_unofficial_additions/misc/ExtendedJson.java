package de.cadentem.pufferfish_unofficial_additions.misc;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.puffish.skillsmod.api.json.JsonElement;
import net.puffish.skillsmod.api.util.Problem;
import net.puffish.skillsmod.api.util.Result;

import java.util.function.Supplier;

public class ExtendedJson {
    public static Result<HolderSet<AbstractSpell>, Problem> parseSpellOrSpellTag(final JsonElement element) {
        return parseSomethingOrSomethingTag(SpellRegistry.REGISTRY.get(), element, () -> "Expected valid spell or spell tag");
    }

    public static Result<HolderSet<SchoolType>, Problem> parseSchoolOrSchoolTag(final JsonElement element) {
        return parseSomethingOrSomethingTag(SchoolRegistry.REGISTRY.get(), element, () -> "Expected valid school or school tag");
    }

    @SuppressWarnings("ConstantConditions")
    private static <T> Result<HolderSet<T>, Problem> parseSomethingOrSomethingTag(final IForgeRegistry<T> registry, final JsonElement element, final Supplier<String> message) {
        try {
            String string = element.getJson().getAsString();

            // The spell / school registries do not support tags
            if (string.startsWith("#")) {
                return Result.failure(element.getPath().createProblem("Tags are not supported for the " + registry.getRegistryName() + " registry"));
            }

            // Holders are not supported as well
            return Result.success(HolderSet.direct(Holder.direct(registry.getValue(new ResourceLocation(string)))));
        } catch (Exception ignored) {
            return Result.failure(element.getPath().createProblem(message.get()));
        }
    }
}
