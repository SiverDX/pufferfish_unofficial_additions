package de.cadentem.pufferfish_unofficial_additions.harvestwithease;

import crystalspider.harvestwithease.api.event.HarvestWithEaseEvent;
import de.cadentem.pufferfish_unofficial_additions.experience.HarvestExperienceSource;
import net.puffish.skillsmod.SkillsMod;

public class ModEvents {
    public static void handleHarvestEvent(final HarvestWithEaseEvent.HarvestDrops event) {
        SkillsMod.getInstance().visitExperienceSources(event.getEntity(), experienceSource ->
                experienceSource instanceof HarvestExperienceSource harvestExperienceSource ? harvestExperienceSource.getValue(event.getEntity(), event.getTargetBlock(), event.getEntity().getMainHandItem(), event.drops) : 0
        );
    }
}