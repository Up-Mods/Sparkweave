package dev.upcraft.sparkweave.api.datagen.provider;

import com.mojang.serialization.Lifecycle;
import net.minecraft.Util;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public abstract class SparkweaveBiomeProvider extends SparkweaveDynamicRegistryEntryProvider {

	protected abstract void generateBiomes(Context ctx, HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers);

	@Override
	public final void generate(RegistrySetBuilder builder) {
		builder.add(Registries.BIOME, bootstrapContext -> {
			var placedFeatures = bootstrapContext.lookup(Registries.PLACED_FEATURE);
			var worldCarvers = bootstrapContext.lookup(Registries.CONFIGURED_CARVER);
			generateBiomes(new SparkweaveBiomeProvider.Context(bootstrapContext), placedFeatures, worldCarvers);
		});
	}

	@Override
	public String getName() {
		return "Biomes";
	}

	public class Context extends SparkweaveDynamicRegistryEntryProvider.Context<Biome> {

		protected Context(BootstrapContext<Biome> bootstrapContext) {
			super(bootstrapContext);
		}

		public void register(ResourceKey<Biome> key, Biome value, Lifecycle lifecycle, String name) {
			bootstrapContext.register(key, value, lifecycle);
			addTranslation(Util.makeDescriptionId("biome", key.location()), name);
		}

		public void register(ResourceKey<Biome> key, Biome value, String name) {
			register(key, value, Lifecycle.stable(), name);
		}
	}
}
