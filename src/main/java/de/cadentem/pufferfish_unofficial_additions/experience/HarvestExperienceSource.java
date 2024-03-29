package de.cadentem.pufferfish_unofficial_additions.experience;

import de.cadentem.pufferfish_unofficial_additions.PUA;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.puffish.skillsmod.api.SkillsAPI;
import net.puffish.skillsmod.api.config.ConfigContext;
import net.puffish.skillsmod.api.experience.ExperienceSource;
import net.puffish.skillsmod.api.experience.calculation.condition.*;
import net.puffish.skillsmod.api.experience.calculation.parameter.ParameterFactory;
import net.puffish.skillsmod.api.json.JsonObjectWrapper;
import net.puffish.skillsmod.api.utils.Result;
import net.puffish.skillsmod.api.utils.failure.Failure;
import net.puffish.skillsmod.experience.calculation.CalculationManager;

import java.util.List;
import java.util.Map;

public class HarvestExperienceSource implements ExperienceSource {
    public static final ResourceLocation ID = new ResourceLocation(PUA.MODID, "harvest_crops");

    private static final Map<String, ConditionFactory<Context>> CONDITIONS = Map.ofEntries(
            Map.entry("block", BlockCondition.factory().map(c -> c.map(Context::state))),
            Map.entry("block_state", BlockStateCondition.factory().map(c -> c.map(Context::state))),
            Map.entry("block_tag", BlockTagCondition.factory().map(c -> c.map(Context::state))),
            Map.entry("tool", ItemCondition.factory().map(c -> c.map(Context::tool))),
            Map.entry("tool_nbt", ItemNbtCondition.factory().map(c -> c.map(Context::tool))),
            Map.entry("tool_tag", ItemTagCondition.factory().map(c -> c.map(Context::tool)))
    );

    private static final Map<String, ParameterFactory<Context>> PARAMETERS = Map.ofEntries(
            Map.entry("dropped_seeds", ParameterFactory.simple(Context::droppedSeeds)),
            Map.entry("dropped_crops", ParameterFactory.simple(Context::droppedCrops))
    );

    private final CalculationManager<Context> manager;

    private HarvestExperienceSource(final CalculationManager<Context> calculated) {
        this.manager = calculated;
    }

    public static void register() {
        SkillsAPI.registerExperienceSourceWithData(ID, (json, context) -> json.getAsObject().andThen(rootObject -> create(rootObject, context)));
    }

    private static Result<HarvestExperienceSource, Failure> create(final JsonObjectWrapper rootObject, final ConfigContext context) {
        return CalculationManager.create(rootObject, CONDITIONS, PARAMETERS, context).mapSuccess(HarvestExperienceSource::new);
    }

    public int getValue(final ServerPlayer player, final BlockState blockState, final ItemStack tool, final List<ItemStack> generatedLoot) {
        return this.manager.getValue(new Context(player, blockState, tool, generatedLoot));
    }

    @Override
    public void dispose(final MinecraftServer server) { /* Nothing to do */ }

    private record Context(ServerPlayer player, BlockState state, ItemStack tool, List<ItemStack> loot) {
        public double droppedSeeds() {
            return loot.stream().filter(item -> item.is(Tags.Items.SEEDS)).mapToInt(ItemStack::getCount).sum();
        }

        public double droppedCrops() {
            return loot.stream().filter(item -> item.is(Tags.Items.CROPS)).mapToInt(ItemStack::getCount).sum();
        }
    }
}
