package dev.upcraft.sparkweave.testmod.datagen.common;

import dev.upcraft.sparkweave.api.datagen.provider.common.SparkweaveTagsProvider;
import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import dev.upcraft.sparkweave.testmod.data.TestmodBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;

public class TestmodBiomeTagsProvider extends SparkweaveTagsProvider<Biome> {
	public TestmodBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, Registries.BIOME, SparkweaveTestmod.MODID, lookupProvider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		existingTag(BiomeTags.HAS_VILLAGE_PLAINS)
			.add(TestmodBiomes.TEST_BIOME);
	}
}
