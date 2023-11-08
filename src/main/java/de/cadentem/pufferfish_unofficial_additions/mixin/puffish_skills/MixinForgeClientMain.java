package de.cadentem.pufferfish_unofficial_additions.mixin.puffish_skills;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.puffish.skillsmod.client.keybinding.KeyBindingHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.puffish.skillsmod.main.ForgeClientMain$KeyBindingReceiverImpl", remap = false)
public abstract class MixinForgeClientMain {
    /** Can be null during datagen */
    @Inject(method = "registerKeyBinding", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getInstance()Lnet/minecraft/client/Minecraft;", shift = At.Shift.BEFORE, remap = true), cancellable = true)
    private void avoidNullPointer(final KeyMapping keyBinding, final KeyBindingHandler handler, final CallbackInfo callback) {
        if (Minecraft.getInstance() == null) {
            callback.cancel();
        }
    }
}
