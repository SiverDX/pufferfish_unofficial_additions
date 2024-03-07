package de.cadentem.pufferfish_unofficial_additions.datagen;

import de.cadentem.pufferfish_unofficial_additions.PUA;
import de.cadentem.pufferfish_unofficial_additions.irons_spellbooks.ISAttributes;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.LanguageProvider;

public class PUALanguageProvider extends LanguageProvider {
    public PUALanguageProvider(final DataGenerator generator, final String locale) {
        super(generator, PUA.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("ui." + PUA.MODID + ".cast_error_no_spell_level", "You do not possess the knowledge required to cast this spell");

        add("attribute." + PUA.MODID + ".fishing_lure", "Fishing Lure");
        add("attribute." + PUA.MODID + ".fishing_lure.desc", "Modifies the fishing lure level of the player");
        add("attribute." + PUA.MODID + ".fishing_luck", "Fishing Luck");
        add("attribute." + PUA.MODID + ".fishing_luck.desc", "Modifies the fishing luck level of the player");
        add("attribute." + PUA.MODID + ".looting", "Looting");
        add("attribute." + PUA.MODID + ".looting.desc", "Modifies the looting level of the player");
        add("attribute." + PUA.MODID + ".respiration", "Respiration");
        add("attribute." + PUA.MODID + ".respiration.desc", "Modifies the respiration level of the player");
        add("attribute." + PUA.MODID + ".harvest", "Harvesting");
        add("attribute." + PUA.MODID + ".harvest.desc", "Modifies the amount of harvested crops");

        add("attribute." + PUA.MODID + ".keep_scroll", "Keep Scroll");
        add("attribute." + PUA.MODID + ".keep_scroll.desc", "Chance to not use up a spell scroll");

        ISAttributes.ATTRIBUTES.getEntries().forEach(attribute -> {
            if (attribute == ISAttributes.KEEP_SCROLL) {
                return;
            }

            ResourceLocation location = attribute.getKey().location();
            String path = location.getPath();
            StringBuilder readable = new StringBuilder();

            if (path.contains("spell_general")) {
                readable.append("General Spell Level");
                add("attribute." + location.toLanguageKey(), readable.toString());
                add("attribute." + location.toLanguageKey() + ".desc", "Modifies the level of all spells");

                return;
            }

            String[] split = path.split("_");

            for (/* Skip prefix (e.g. `spell_type_`) */ int i = 2; i < split.length; i++) {
                readable.append(Character.toUpperCase(split[i].charAt(0)));
                readable.append(split[i].substring(1));

                if (i != split.length - 1) {
                    readable.append(" ");
                } else {
                    if (path.contains("spell_school")) {
                        add("attribute." + location.toLanguageKey(), readable.append(" School Level").toString());
                        add("attribute." + location.toLanguageKey() + ".desc", "Modifies the level of all spells of this school");
                    } else if (path.contains("spell_type")) {
                        add("attribute." + location.toLanguageKey(), readable.append(" Spell Level").toString());
                        add("attribute." + location.toLanguageKey() + ".desc", "Modifies the level of this spell");
                    }
                }
            }
        });
    }
}
