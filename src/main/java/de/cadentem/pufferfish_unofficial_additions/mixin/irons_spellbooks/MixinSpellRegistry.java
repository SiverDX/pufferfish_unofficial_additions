package de.cadentem.pufferfish_unofficial_additions.mixin.irons_spellbooks;

import de.cadentem.pufferfish_unofficial_additions.irons_spellbooks.Attributes;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SpellRegistry.class, remap = false)
public abstract class MixinSpellRegistry {
    @Inject(method = "registerSpell", at = @At("RETURN"))
    private static void pufferfish_unofficial_additions$registerSpellAttribute(final AbstractSpell spell, final CallbackInfoReturnable<RegistryObject<AbstractSpell>> callback) {
        Attributes.createAttribute("spell_type_" + spell.getSpellName());
    }
}
