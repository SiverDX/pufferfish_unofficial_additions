package de.cadentem.pufferfish_unofficial_additions.mixin;

import de.cadentem.pufferfish_unofficial_additions.registry.PUAAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FishingRodItem.class)
public abstract class MixinFishingRod {
    @ModifyVariable(method = "use", at = @At(value = "STORE"), name = "k")
    private int pufferfish_unofficial_additions$amplifyLure(int original, /* Method arguments: */ final Level level, final Player player) {
        return PUAAttributes.getIntValue(player, PUAAttributes.FISHING_LURE.get(), original);
    }

    @ModifyVariable(method = "use", at = @At(value = "STORE"), name = "j")
    private int pufferfish_unofficial_additions$amplifyLuck(int original, /* Method arguments: */ final Level level, final Player player) {
        return PUAAttributes.getIntValue(player, PUAAttributes.FISHING_LUCK.get(), original);
    }
}
