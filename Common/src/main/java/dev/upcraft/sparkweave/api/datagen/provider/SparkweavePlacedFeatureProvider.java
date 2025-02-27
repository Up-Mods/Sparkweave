package dev.upcraft.sparkweave.api.datagen.provider;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public abstract class SparkweavePlacedFeatureProvider extends SparkweaveDynamicRegistryEntryProvider {

	protected abstract void generatePlacedFeatures(SparkweavePlacedFeatureProvider.Context ctx, HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures);

	@Override
	public final void generate(RegistrySetBuilder builder) {
		builder.add(Registries.PLACED_FEATURE, bootstrapContext -> {
			var configuredFeatures = bootstrapContext.lookup(Registries.CONFIGURED_FEATURE);
			generatePlacedFeatures(new Context(bootstrapContext), configuredFeatures);
		});
	}

	@Override
	public String getName() {
		return "PlacedFeatures";
	}

	public static class Context extends SparkweaveDynamicRegistryEntryProvider.Context<PlacedFeature> {

		protected Context(BootstrapContext<PlacedFeature> bootstrapContext) {
			super(bootstrapContext);
		}

		public void register(ResourceKey<PlacedFeature> key, PlacedFeature value, Lifecycle lifecycle) {
			bootstrapContext.register(key, value, lifecycle);
		}

		public void register(ResourceKey<PlacedFeature> key, PlacedFeature value) {
			register(key, value, Lifecycle.stable());
		}
	}
}
