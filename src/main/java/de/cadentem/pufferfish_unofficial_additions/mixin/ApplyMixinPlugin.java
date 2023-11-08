package de.cadentem.pufferfish_unofficial_additions.mixin;

import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ApplyMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(final String mixinPackage) { /* Nothing to do */ }

    @Override
    public String getRefMapperConfig() { /* Nothing to do */ return null; }

    @Override
    public boolean shouldApplyMixin(final String targetClassName, final String mixinClassName) {
        String modid = mixinClassName
                .replace("de.cadentem.pufferfish_unofficial_additions.mixin.", "") // General package
                .replaceAll("\\.?Mixin.*", "") // Mixin class name
                .replaceAll("\\..*", ""); // Sub package

        if (!modid.isBlank()) {
            // `ModList.get()` is not available at this point in time
            return LoadingModList.get().getModFileById(modid) != null;
        }

        return true;
    }

    @Override
    public void acceptTargets(final Set<String> myTargets, final Set<String> otherTargets) { /* Nothing to do */ }

    @Override
    public List<String> getMixins() { /* Nothing to do */ return null; }

    @Override
    public void preApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) { /* Nothing to do */ }

    @Override
    public void postApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) { /* Nothing to do */ }
}