package de.cadentem.pufferfish_unofficial_additions.mixin.irons_spellbooks;

import de.cadentem.pufferfish_unofficial_additions.experience.irons_spellbooks.SpellCastingExperience;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.puffish.skillsmod.SkillsMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractSpell.class, remap = false)
public abstract class MixinAbstractSpell {
    @Inject(method = "castSpell", at = @At("HEAD"))
    private void pufferfish_unofficial_additions$handleExperienceSource(final Level world, int spellLevel, final ServerPlayer player, final CastSource castSource, boolean triggerCooldown, final CallbackInfo callback) {
        SkillsMod.getInstance().visitExperienceSources(player, experienceSource -> experienceSource instanceof SpellCastingExperience spellCastingExperience ? spellCastingExperience.getValue(player, getSchoolType(), getSpellId(), spellLevel, castSource) : 0);
    }

    @Shadow public abstract String getSpellId();
    @Shadow public abstract SchoolType getSchoolType();
}
