package dev.upcraft.sparkweave.testmod.data;

import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class TestmodPlacedFeatures {

	public static final ResourceKey<PlacedFeature> OBSIDIAN_CLUSTERS = ResourceKey.create(Registries.PLACED_FEATURE, SparkweaveTestmod.id("obsidian_clusters"));
}
