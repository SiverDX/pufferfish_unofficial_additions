package de.cadentem.pufferfish_unofficial_additions.events;

import de.cadentem.pufferfish_unofficial_additions.experience.irons_spellbooks.SpellCastingExperienceSource;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import net.minecraft.server.level.ServerPlayer;
import net.puffish.skillsmod.api.SkillsAPI;

public class ISSEvents {
    public static void grantSpellExperience(final SpellOnCastEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) {
            return;
        }

        SkillsAPI.updateExperienceSources(serverPlayer, SpellCastingExperienceSource.class, source -> source.getValue(serverPlayer, event.getSchoolType(), event.getSpellId(), event.getSpellLevel(), event.getCastSource()));
    }
}
