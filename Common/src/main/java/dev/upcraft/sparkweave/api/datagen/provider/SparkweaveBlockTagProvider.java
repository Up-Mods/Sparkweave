package dev.upcraft.sparkweave.api.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.CompletableFuture;

@ApiStatus.Experimental
public abstract class SparkweaveBlockTagProvider extends SparkweaveIntrinsicHolderTagsProvider<Block> {
	@SuppressWarnings("deprecation")
	public SparkweaveBlockTagProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> parentProvider) {
		super(output, Registries.BLOCK, modId, lookupProvider, parentProvider, block -> block.builtInRegistryHolder().key());
	}

	public SparkweaveBlockTagProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		this(output, modId, lookupProvider, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()));
	}
}
