package de.cadentem.pufferfish_unofficial_additions.experience;

import de.cadentem.pufferfish_unofficial_additions.PUA;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.puffish.skillsmod.api.SkillsAPI;
import net.puffish.skillsmod.api.config.ConfigContext;
import net.puffish.skillsmod.api.experience.ExperienceSource;
import net.puffish.skillsmod.api.experience.calculation.condition.ConditionFactory;
import net.puffish.skillsmod.api.experience.calculation.condition.ItemCondition;
import net.puffish.skillsmod.api.experience.calculation.condition.ItemNbtCondition;
import net.puffish.skillsmod.api.experience.calculation.condition.ItemTagCondition;
import net.puffish.skillsmod.api.experience.calculation.parameter.ParameterFactory;
import net.puffish.skillsmod.api.json.JsonObjectWrapper;
import net.puffish.skillsmod.api.utils.Failure;
import net.puffish.skillsmod.api.utils.Result;
import net.puffish.skillsmod.experience.calculation.CalculationManager;

import java.util.Map;

public class FishingExperienceSource implements ExperienceSource {
    public static final ResourceLocation ID = new ResourceLocation(PUA.MODID, "fishing");

    private static final Map<String, ConditionFactory<Context>> CONDITIONS = Map.ofEntries(
            Map.entry("tool", ItemCondition.factory().map(c -> c.map(Context::tool))),
            Map.entry("tool_nbt", ItemNbtCondition.factory().map(c -> c.map(Context::tool))),
            Map.entry("tool_tag", ItemTagCondition.factory().map(c -> c.map(Context::tool))),
            Map.entry("fished", ItemCondition.factory().map(c -> c.map(Context::fishedItem))),
            Map.entry("fished_nbt", ItemNbtCondition.factory().map(c -> c.map(Context::fishedItem))),
            Map.entry("fished_tag", ItemTagCondition.factory().map(c -> c.map(Context::fishedItem)))
    );

    private static final Map<String, ParameterFactory<Context>> PARAMETERS = Map.ofEntries(
            Map.entry("fished_amount", ParameterFactory.simple(Context::fishedAmount))
    );

    private final CalculationManager<Context> manager;

    private FishingExperienceSource(final CalculationManager<Context> calculated) {
        this.manager = calculated;
    }

    public static void register() {
        SkillsAPI.registerExperienceSourceWithData(ID, (json, context) -> json.getAsObject().andThen(rootObject -> create(rootObject, context)));
    }

    private static Result<FishingExperienceSource, Failure> create(final JsonObjectWrapper rootObject, final ConfigContext context) {
        return CalculationManager.create(rootObject, CONDITIONS, PARAMETERS, context).mapSuccess(FishingExperienceSource::new);
    }

    public int getValue(final ServerPlayer player, final ItemStack tool, final ItemStack fishedItem) {
        return this.manager.getValue(new Context(player, tool, fishedItem));
    }

    @Override
    public void dispose(MinecraftServer minecraftServer) { /* Nothing to do */ }

    private record Context(ServerPlayer player, ItemStack tool, ItemStack fishedItem) {
        public double fishedAmount() {
            return fishedItem.getCount();
        }
    }
}
