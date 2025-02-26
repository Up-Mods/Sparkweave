package dev.upcraft.sparkweave.testmod.datagen.common;

import dev.upcraft.sparkweave.api.datagen.provider.BiomeProvider;
import dev.upcraft.sparkweave.testmod.data.TestmodBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class TestmodBiomeProvider extends BiomeProvider {

	@Override
	protected void generateBiomes(Context ctx, HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
		ctx.register(TestmodBiomes.TEST_BIOME, OverworldBiomes.biome(
			true,
			0.5F,
			0.5F,
			new MobSpawnSettings.Builder(),
			new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers),
			null
			), "Test Biome");
	}
}
