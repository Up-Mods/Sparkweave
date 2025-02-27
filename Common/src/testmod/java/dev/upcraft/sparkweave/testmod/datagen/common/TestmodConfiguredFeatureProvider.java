package dev.upcraft.sparkweave.testmod.datagen.common;

import dev.upcraft.sparkweave.api.datagen.provider.SparkweaveConfiguredFeatureProvider;
import dev.upcraft.sparkweave.testmod.data.TestmodConfiguredFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class TestmodConfiguredFeatureProvider extends SparkweaveConfiguredFeatureProvider {
	@Override
	protected void generateConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
		RuleTest replaceStone = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);

		FeatureUtils.register(ctx, TestmodConfiguredFeatures.OBSIDIAN_CLUSTERS, Feature.ORE, new OreConfiguration(replaceStone, Blocks.OBSIDIAN.defaultBlockState(), 64));
	}
}
