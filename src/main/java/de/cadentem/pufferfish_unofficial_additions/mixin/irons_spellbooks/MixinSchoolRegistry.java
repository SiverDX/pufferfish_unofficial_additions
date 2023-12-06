package de.cadentem.pufferfish_unofficial_additions.mixin.irons_spellbooks;

import de.cadentem.pufferfish_unofficial_additions.irons_spellbooks.ISAttributes;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SchoolRegistry.class, remap = false)
public abstract class MixinSchoolRegistry {
    @Inject(method = "registerSchool", at = @At("RETURN"))
    private static void pufferfish_unofficial_additions$registerSchool(final SchoolType schoolType, final CallbackInfoReturnable<RegistryObject<SchoolType>> callback) {
        ISAttributes.createAttribute("spell_school_" + schoolType.getId().getPath());
    }
}
