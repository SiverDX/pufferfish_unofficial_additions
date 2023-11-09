package de.cadentem.pufferfish_unofficial_additions.mixin.aquaculture;

import com.teammetallurgy.aquaculture.entity.AquaFishingBobberEntity;
import de.cadentem.pufferfish_unofficial_additions.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AquaFishingBobberEntity.class)
public abstract class MixinAquaFishingBobberEntity extends FishingHook {
    public MixinAquaFishingBobberEntity(final Player player, final Level level, int luck, int lureSpeed) {
        super(player, level, luck, lureSpeed);
    }

    @Inject(method = "catchingFish", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, ordinal = 11, shift = At.Shift.AFTER))
    private void fixLure(final BlockPos position, final CallbackInfo callback) {
        timeUntilLured = Utils.getTicksCaughtDelay(this);
    }
}
