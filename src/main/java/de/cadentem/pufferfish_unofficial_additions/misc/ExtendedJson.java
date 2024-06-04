package de.cadentem.pufferfish_unofficial_additions.misc;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.IForgeRegistry;
import net.puffish.skillsmod.api.json.JsonElement;
import net.puffish.skillsmod.api.util.Problem;
import net.puffish.skillsmod.api.util.Result;

import java.util.function.Supplier;

public class ExtendedJson {
    public static Result<AbstractSpell, Problem> parseSpell(final JsonElement element) {
        return parseSomething(SpellRegistry.REGISTRY.get(), element, () -> "Expected valid spell");
    }

    public static Result<HolderSet<AbstractSpell>, Problem> parseSpellTag(final JsonElement element) {
        return parseSomethingTag(SpellRegistry.REGISTRY.get(), element, () -> "Expected valid spell tag");
    }

    public static Result<HolderSet<AbstractSpell>, Problem> parseSpellOrSpellTag(final JsonElement element) {
        return parseSomethingOrSomethingTag(SpellRegistry.REGISTRY.get(), element, () -> "Expected valid spell or spell tag");
    }

    public static Result<SchoolType, Problem> parseSchool(final JsonElement element) {
        return parseSomething(SchoolRegistry.REGISTRY.get(), element, () -> "Expected valid school");
    }

    public static Result<HolderSet<SchoolType>, Problem> parseSchoolTag(final JsonElement element) {
        return parseSomethingTag(SchoolRegistry.REGISTRY.get(), element, () -> "Expected valid school tag");
    }

    public static Result<HolderSet<SchoolType>, Problem> parseSchoolOrSchoolTag(final JsonElement element) {
        return parseSomethingOrSomethingTag(SchoolRegistry.REGISTRY.get(), element, () -> "Expected valid school or school tag");
    }

    // Modified from 'net.puffish.skillsmod.api.json.BuiltinJson'
    private static <T> Result<T, Problem> parseSomething(final IForgeRegistry<T> registry, final JsonElement element, final Supplier<String> message) {
        try {
            String string = element.getJson().getAsString();
            return Result.success(registry.getDelegateOrThrow(new ResourceLocation(string)).get());
        } catch (Exception ignored) {
            return Result.failure(element.getPath().createProblem(message.get()));
        }
    }

    @SuppressWarnings("ConstantConditions")
    private static <T> Result<HolderSet<T>, Problem> parseSomethingTag(final IForgeRegistry<T> registry, final JsonElement element, final Supplier<String> message) {
        try {
            String string = element.getJson().getAsString();
            if (string.startsWith("#")) {
                string = string.substring(1);
            }

            return Result.success(HolderSet.direct(registry.tags().getTag(TagKey.create(registry.getRegistryKey(), new ResourceLocation(string))).stream().map(Holder::direct).toList()));
        } catch (Exception ignored) {
            return Result.failure(element.getPath().createProblem(message.get()));
        }
    }

    // TODO :: Tags are not supported for School / Spell registries and the Holder methods return no value
    @SuppressWarnings("ConstantConditions")
    private static <T> Result<HolderSet<T>, Problem> parseSomethingOrSomethingTag(final IForgeRegistry<T> registry, final JsonElement element, final Supplier<String> message) {
        try {
            String string = element.getJson().getAsString();
            return string.startsWith("#")
                    ? Result.success(HolderSet.direct(registry.tags().getTag(TagKey.create(registry.getRegistryKey(), new ResourceLocation(string.substring(1)))).stream().map(Holder::direct).toList()))
//                    : Result.success(HolderSet.direct(registry.getHolder(ResourceKey.create(registry.getRegistryKey(), new ResourceLocation(string))).orElseThrow()));
                    : Result.success(HolderSet.direct(Holder.direct(registry.getValue(new ResourceLocation(string)))));
        } catch (Exception ignored) {
            return Result.failure(element.getPath().createProblem(message.get()));
        }
    }
}
