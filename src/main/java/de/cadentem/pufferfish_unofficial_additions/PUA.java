package de.cadentem.pufferfish_unofficial_additions;

import com.mojang.logging.LogUtils;
import de.cadentem.pufferfish_unofficial_additions.experience.FishingExperienceSource;
import de.cadentem.pufferfish_unofficial_additions.experience.HarvestExperienceSource;
import de.cadentem.pufferfish_unofficial_additions.experience.irons_spellbooks.SpellCastingExperience;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(PUA.MODID)
public class PUA {
    public static final String MODID = "pufferfish_unofficial_additions";
    public static final Logger LOG = LogUtils.getLogger();

    public PUA() {
        HarvestExperienceSource.register();
        FishingExperienceSource.register();

        if (ModList.get().isLoaded("irons_spellbooks")) {
            SpellCastingExperience.register();
        }
    }
}
