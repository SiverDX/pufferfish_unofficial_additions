package de.cadentem.pufferfish_unofficial_additions.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import de.cadentem.pufferfish_unofficial_additions.experience.HarvestExperienceSource;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.puffish.skillsmod.SkillsMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LootTable.class, priority = 1500)
public class MixinLootTable {
    @Inject(method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;", at = @At("RETURN"))
    private void pufferfish_unofficial_additions$applyExperienceSource(final LootContext context, final CallbackInfoReturnable<ObjectArrayList<ItemStack>> callback, @Local final LocalRef<ObjectArrayList<ItemStack>> generatedLoot) {
        if (!context.hasParam(LootContextParams.BLOCK_STATE) || !context.hasParam(LootContextParams.THIS_ENTITY)) {
            return;
        }

        BlockState state = context.getParam(LootContextParams.BLOCK_STATE);
        Entity entity = context.getParam(LootContextParams.THIS_ENTITY);
        ItemStack tool;

        if (context.hasParam(LootContextParams.TOOL)) {
            tool = context.getParam(LootContextParams.TOOL);
        } else {
            tool = ItemStack.EMPTY;
        }

        if (entity instanceof ServerPlayer serverPlayer) {
            SkillsMod.getInstance().visitExperienceSources(serverPlayer, experienceSource ->
                    experienceSource instanceof HarvestExperienceSource harvestExperienceSource ? harvestExperienceSource.getValue(serverPlayer, state, tool, generatedLoot.get()) : 0
            );
        }
    }
}
