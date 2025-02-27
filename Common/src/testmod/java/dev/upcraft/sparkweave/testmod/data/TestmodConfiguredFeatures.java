package dev.upcraft.sparkweave.testmod.data;

import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class TestmodConfiguredFeatures {

	public static final ResourceKey<ConfiguredFeature<?, ?>> OBSIDIAN_CLUSTERS = ResourceKey.create(Registries.CONFIGURED_FEATURE, SparkweaveTestmod.id("obsidian_clusters"));
}
