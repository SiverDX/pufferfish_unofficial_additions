package de.cadentem.pufferfish_unofficial_additions.registry;

import de.cadentem.pufferfish_unofficial_additions.PUA;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PUAAttributes {
    public static final int MAX = 1024;

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.Keys.ATTRIBUTES, PUA.MODID);

    public static final RegistryObject<Attribute> FISHING_LURE = createAttribute("fishing_lure");
    public static final RegistryObject<Attribute> FISHING_LUCK = createAttribute("fishing_luck");
    public static final RegistryObject<Attribute> LOOTING = createAttribute("looting");
    public static final RegistryObject<Attribute> RESPIRATION = createAttribute("respiration");
    public static final RegistryObject<Attribute> HARVEST_BONUS = createAttribute("harvest");

    public static RegistryObject<Attribute> createAttribute(final String id) {
        return ATTRIBUTES.register(id, () -> new RangedAttribute("attribute." + PUA.MODID + "." + id, 0, 0, MAX).setSyncable(true));
    }

    @SubscribeEvent
    public static void setAttributes(final EntityAttributeModificationEvent event) {
        ATTRIBUTES.getEntries().forEach(attribute -> event.add(EntityType.PLAYER, attribute.get()));
    }

    @SuppressWarnings("ConstantConditions")
    public static int getIntValue(final LivingEntity entity, final Attribute attribute, double base) {
        if (/* Can be null */ entity.getAttributes() == null || !entity.getAttributes().hasAttribute(attribute)) {
            return (int) base;
        }

        double value = getAttributeValue(entity, attribute, base);
        int clippedValue = (int) value;

        if (entity.getRandom().nextFloat() < value - clippedValue) {
            value++;
        }

        return (int) value;
    }

    public static double getAttributeValue(final LivingEntity entity, final Attribute attribute, double base) {
        if (attribute == null) {
            return 0;
        }

        AttributeInstance instance = entity.getAttribute(attribute);

        if (instance == null) {
            return 0;
        }

        if (instance.getBaseValue() != base) {
            instance.setBaseValue(base);
        }

        return instance.getValue();
    }
}
