package de.cadentem.pufferfish_unofficial_additions.misc;

import de.cadentem.pufferfish_unofficial_additions.mixin.puffish_skills.BuiltinJsonAccess;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.core.HolderSet;
import net.puffish.skillsmod.api.json.JsonElement;
import net.puffish.skillsmod.api.util.Problem;
import net.puffish.skillsmod.api.util.Result;

public class ExtendedJson {
    public static Result<HolderSet<AbstractSpell>, Problem> parseSpell(final JsonElement element) {
        return BuiltinJsonAccess.pufferfish_unofficial_additions$parseSomethingOrSomethingTag(element, SpellRegistry.REGISTRY, () -> "Expected a valid spell", spell -> "Invalid spell or spell tag [" + spell + "]", resource -> "Unknown spell / spell tag: [" + resource + "]");
    }

    public static Result<HolderSet<SchoolType>, Problem> parseSchool(final JsonElement element) {
        return BuiltinJsonAccess.pufferfish_unofficial_additions$parseSomethingOrSomethingTag(element, SchoolRegistry.REGISTRY, () -> "Expected a valid school", school -> "Invalid school or school tag [" + school + "]", resource -> "Unknown school / school tag: [" + resource + "]");
    }
}
