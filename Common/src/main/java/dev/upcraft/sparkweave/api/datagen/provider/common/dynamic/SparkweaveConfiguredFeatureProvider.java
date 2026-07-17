package dev.upcraft.sparkweave.api.datagen.provider.common.dynamic;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public abstract class SparkweaveConfiguredFeatureProvider extends SparkweaveDynamicRegistryEntryProvider {

	protected abstract void generateConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> ctx);

	@Override
	public final void generate(RegistrySetBuilder builder) {
		builder.add(Registries.CONFIGURED_FEATURE, this::generateConfiguredFeatures);
	}

	@Override
	public String getName() {
		return "ConfiguredFeatures";
	}
}
