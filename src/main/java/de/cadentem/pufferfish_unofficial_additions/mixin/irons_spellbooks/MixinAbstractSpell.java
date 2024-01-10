package de.cadentem.pufferfish_unofficial_additions.mixin.irons_spellbooks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.cadentem.pufferfish_unofficial_additions.PUA;
import de.cadentem.pufferfish_unofficial_additions.experience.irons_spellbooks.SpellCastingExperience;
import de.cadentem.pufferfish_unofficial_additions.irons_spellbooks.ISAttributes;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.puffish.skillsmod.SkillsMod;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = AbstractSpell.class, remap = false)
public abstract class MixinAbstractSpell {
    @Inject(method = "castSpell", at = @At("HEAD"))
    private void pufferfish_unofficial_additions$handleExperienceSource(final Level world, int spellLevel, final ServerPlayer player, final CastSource castSource, boolean triggerCooldown, final CallbackInfo callback) {
        SkillsMod.getInstance().visitExperienceSources(player, experienceSource -> experienceSource instanceof SpellCastingExperience spellCastingExperience ? spellCastingExperience.getValue(player, getSchoolType(), getSpellId(), spellLevel, castSource) : 0);
    }

    @ModifyReturnValue(method = "getLevel", at = @At("RETURN"))
    private int pufferfish_unofficial_additions$applySkill(int original, /* Parameters: */ int level, @Nullable final LivingEntity caster) {
        if (getMaxLevel() == 1) {
            // Usually there is no reason to increase the level in this case
            return original;
        }

        if (caster instanceof Player player) {
            Attribute generalAttribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(PUA.MODID, "spell_general"));
            Attribute schoolAttribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(PUA.MODID, "spell_school_" + getSchoolType().getId().getPath()));
            Attribute spellAttribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(PUA.MODID, "spell_type_" + getSpellName()));

            List<AttributeModifier> addition = new ArrayList<>();
            List<AttributeModifier> multiplyBase = new ArrayList<>();
            List<AttributeModifier> multiplyTotal = new ArrayList<>();

            pufferfish_unofficial_additions$fillModifiers(player, generalAttribute, addition, multiplyBase, multiplyTotal);
            pufferfish_unofficial_additions$fillModifiers(player, schoolAttribute, addition, multiplyBase, multiplyTotal);
            pufferfish_unofficial_additions$fillModifiers(player, spellAttribute, addition, multiplyBase, multiplyTotal);

            double base = original;

            for (AttributeModifier modifier : addition) {
                base += modifier.getAmount();
            }

            double result = base;

            for (AttributeModifier modifier : multiplyBase) {
                result += base * modifier.getAmount();
            }

            for (AttributeModifier modifier : multiplyTotal) {
                result *= 1 + modifier.getAmount();
            }

            return (int) Mth.clamp(result, 0, Math.max(ISAttributes.MAX, original));
        }

        return original;
    }

    @Unique
    private void pufferfish_unofficial_additions$fillModifiers(final LivingEntity livingEntity, final Attribute attribute, final List<AttributeModifier> addition, final List<AttributeModifier> multiplyBase, final List<AttributeModifier> multiplyTotal) {
        if (livingEntity == null || attribute == null) {
            return;
        }

        AttributeInstance instance = livingEntity.getAttribute(attribute);

        if (instance == null) {
            return;
        }

        addition.addAll(instance.getModifiers(AttributeModifier.Operation.ADDITION));
        multiplyBase.addAll(instance.getModifiers(AttributeModifier.Operation.MULTIPLY_BASE));
        multiplyTotal.addAll(instance.getModifiers(AttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    @Shadow public abstract String getSpellId();
    @Shadow public abstract SchoolType getSchoolType();
    @Shadow public abstract String getSpellName();
    @Shadow public abstract int getMaxLevel();
}
