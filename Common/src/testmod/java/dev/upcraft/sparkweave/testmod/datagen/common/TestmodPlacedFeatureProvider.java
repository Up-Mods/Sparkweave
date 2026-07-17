package dev.upcraft.sparkweave.testmod.datagen.common;

import dev.upcraft.sparkweave.api.datagen.provider.common.dynamic.SparkweavePlacedFeatureProvider;
import dev.upcraft.sparkweave.api.worldgen.feature.GridPlacementFilter;
import dev.upcraft.sparkweave.testmod.data.TestmodConfiguredFeatures;
import dev.upcraft.sparkweave.testmod.data.TestmodPlacedFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.List;

public class TestmodPlacedFeatureProvider extends SparkweavePlacedFeatureProvider {

	@Override
	protected void generatePlacedFeatures(Context ctx, HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures) {
		var obsidianClusterFeature = configuredFeatures.getOrThrow(TestmodConfiguredFeatures.OBSIDIAN_CLUSTERS);
		ctx.register(TestmodPlacedFeatures.OBSIDIAN_CLUSTERS, new PlacedFeature(obsidianClusterFeature, List.of(new GridPlacementFilter(RandomSpreadType.LINEAR, 13, 2, 1940516340), InSquarePlacement.spread(), BiomeFilter.biome())));
	}
}
