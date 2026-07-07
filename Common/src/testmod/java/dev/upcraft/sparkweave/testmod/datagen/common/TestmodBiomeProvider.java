package dev.upcraft.sparkweave.testmod.datagen.common;

import dev.upcraft.sparkweave.api.datagen.provider.SparkweaveBiomeProvider;
import dev.upcraft.sparkweave.testmod.data.TestmodBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.util.Util;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class TestmodBiomeProvider extends SparkweaveBiomeProvider {

	@Override
	protected void generateBiomes(Context ctx, HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
		ctx.register(TestmodBiomes.TEST_BIOME, OverworldBiomes.baseBiome(0.5F, 0.5F)
			.hasPrecipitation(true)
			.mobSpawnSettings(Util.make(new MobSpawnSettings.Builder(), builder -> {
				BiomeDefaultFeatures.commonSpawns(builder);
			}).build())
			.generationSettings(Util.make(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers), builder -> {
				OverworldBiomes.globalOverworldGeneration(builder);
				BiomeDefaultFeatures.addDefaultOres(builder);
				BiomeDefaultFeatures.addDefaultSoftDisks(builder);
				BiomeDefaultFeatures.addDefaultFlowers(builder);
				BiomeDefaultFeatures.addDefaultMushrooms(builder);
				BiomeDefaultFeatures.addDefaultExtraVegetation(builder, false);
				BiomeDefaultFeatures.addCommonBerryBushes(builder);
				BiomeDefaultFeatures.addAncientDebris(builder);
			}).build())
			.build(), "Test Biome");
	}
}
