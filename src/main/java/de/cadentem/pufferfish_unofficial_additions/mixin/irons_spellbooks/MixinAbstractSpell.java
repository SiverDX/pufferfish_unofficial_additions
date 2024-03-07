package de.cadentem.pufferfish_unofficial_additions.mixin.irons_spellbooks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.cadentem.pufferfish_unofficial_additions.experience.irons_spellbooks.SpellCastingExperience;
import de.cadentem.pufferfish_unofficial_additions.utils.SpellUtils;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastResult;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.puffish.skillsmod.SkillsMod;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractSpell.class, remap = false)
public abstract class MixinAbstractSpell {
    @Unique private static final CastResult pufferfish_unofficial_additions$NO_SPELL_LEVEL_FAILURE = new CastResult(CastResult.Type.FAILURE, Component.translatable("ui.pufferfish_unofficial_additions.cast_error_no_spell_level"));

    @Inject(method = "castSpell", at = @At("HEAD"))
    private void pufferfish_unofficial_additions$handleExperienceSource(final Level world, int spellLevel, final ServerPlayer player, final CastSource castSource, boolean triggerCooldown, final CallbackInfo callback) {
        SkillsMod.getInstance().visitExperienceSources(player, experienceSource -> experienceSource instanceof SpellCastingExperience spellCastingExperience ? spellCastingExperience.getValue(player, getSchoolType(), getSpellId(), spellLevel, castSource) : 0);
    }

    @Inject(method = "canBeCastedBy", at = @At("HEAD"), cancellable = true)
    private void pufferfish_unofficial_additions$cancelForNoSpellLevel(int spellLevel, final CastSource castSource, final MagicData magicData, final Player player, final CallbackInfoReturnable<CastResult> callback) {
        if (SpellUtils.calculateSpellLevel(player, (AbstractSpell) (Object) this, spellLevel) == 0) {
            callback.setReturnValue(pufferfish_unofficial_additions$NO_SPELL_LEVEL_FAILURE);
        }
    }

    @ModifyReturnValue(method = "getLevel", at = @At("RETURN"))
    private int pufferfish_unofficial_additions$applySkill(int original, /* Parameters: */ int level, @Nullable final LivingEntity caster) {
        if (caster instanceof Player player) {
            return SpellUtils.calculateSpellLevel(player, (AbstractSpell) (Object) this, original);
        }

        return original;
    }

    @Shadow public abstract String getSpellId();
    @Shadow public abstract SchoolType getSchoolType();
}
