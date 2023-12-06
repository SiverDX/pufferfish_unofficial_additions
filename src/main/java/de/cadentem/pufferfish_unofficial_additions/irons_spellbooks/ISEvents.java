package de.cadentem.pufferfish_unofficial_additions.irons_spellbooks;

import de.cadentem.pufferfish_unofficial_additions.experience.irons_spellbooks.SpellCastingExperience;
import io.redspace.ironsspellbooks.api.events.SpellCastEvent;
import net.minecraft.server.level.ServerPlayer;
import net.puffish.skillsmod.SkillsMod;

public class ISEvents {
    public static void handleSpellCast(final SpellCastEvent event) {
        if (!event.isCanceled() && event.getEntity() instanceof ServerPlayer serverPlayer) {
            SkillsMod.getInstance().visitExperienceSources(serverPlayer, experienceSource -> experienceSource instanceof SpellCastingExperience spellCastingExperience ? spellCastingExperience.getValue(serverPlayer, event.getSchoolType(), event.getSpellId(), event.getSpellLevel()) : 0);
        }
    }
}
