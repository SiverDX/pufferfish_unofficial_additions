package de.cadentem.pufferfish_unofficial_additions.utils;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.FishingHook;

public class Utils {
    /** <a href="https://github.com/Shadows-of-Fire/Apotheosis/blob/1.20/src/main/java/dev/shadowsoffire/apotheosis/ench/asm/EnchHooks.java">Source</a> */
    public static int getTicksCaughtDelay(final FishingHook bobber) {
        int lowBound = Math.max(1, 100 - bobber.lureSpeed * 10);
        int highBound = Math.max(lowBound, 600 - bobber.lureSpeed * 60);
        return Mth.nextInt(bobber.random, lowBound, highBound);
    }
}
