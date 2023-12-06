package de.cadentem.pufferfish_unofficial_additions.irons_spellbooks;

import de.cadentem.pufferfish_unofficial_additions.PUA;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ISAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, PUA.MODID);
    public static final int MAX = 100;

    public static RegistryObject<Attribute> KEEP_SCROLL = ATTRIBUTES.register("keep_scroll", () -> new RangedAttribute("attribute." + PUA.MODID + ".keep_scroll", 0, 0, 1).setSyncable(true));

    static {
        createAttribute("spell_general");
    }

    public static void createAttribute(final String id) {
        ATTRIBUTES.register(id, () -> new RangedAttribute("attribute." + PUA.MODID + "." + id, 0, 0, MAX).setSyncable(true));
        PUA.LOG.debug("Registered attribute [{}]", id);
    }

    public static void setAttributes(final EntityAttributeModificationEvent event) {
        ATTRIBUTES.getEntries().forEach(attribute -> event.add(EntityType.PLAYER, attribute.get()));
    }
}
