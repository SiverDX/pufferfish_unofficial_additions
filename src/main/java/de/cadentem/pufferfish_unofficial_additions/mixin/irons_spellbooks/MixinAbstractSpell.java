package de.cadentem.pufferfish_unofficial_additions.mixin.irons_spellbooks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.cadentem.pufferfish_unofficial_additions.PUA;
import de.cadentem.pufferfish_unofficial_additions.irons_spellbooks.Attributes;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = AbstractSpell.class, remap = false)
public abstract class MixinAbstractSpell {
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

            return (int) Mth.clamp(result, 0, Math.max(Attributes.MAX, original));
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

    @Shadow public abstract SchoolType getSchoolType();
    @Shadow public abstract String getSpellName();
    @Shadow public abstract int getMaxLevel();
}
