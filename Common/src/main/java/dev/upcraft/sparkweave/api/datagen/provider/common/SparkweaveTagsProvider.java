package dev.upcraft.sparkweave.api.datagen.provider.common;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;

public abstract class SparkweaveTagsProvider<T> extends TagsProvider<T> {

	/**
	 * @param modId the ID of the mod this generator belongs to
	 */
	@SuppressWarnings({"deprecation", "RedundantSuppression"}) // needed for NeoForge because of patched modId parameter
	public SparkweaveTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<T>> parentProvider) {
		super(output, registryKey, lookupProvider, parentProvider);
	}

	public SparkweaveTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		this(output, registryKey, modId, lookupProvider, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()));
	}

	/**
	 * @deprecated use {@link #getOrCreateRawBuilder(TagKey, String)}
	 */
	@Deprecated
	@Override
	public TagBuilder getOrCreateRawBuilder(TagKey<T> tag) {
		return super.getOrCreateRawBuilder(tag);
	}

	public TagBuilder getOrCreateRawBuilder(TagKey<T> tag, String tagName) {
		// TODO add tag translation to language file
		return super.getOrCreateRawBuilder(tag);
	}

	// TODO getName()
}
