package dev.upcraft.sparkweave.api.datagen.provider.common;

import dev.upcraft.sparkweave.mixin.datagen.IntrinsicHolderTagsProviderAcessor;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@ApiStatus.Experimental
public abstract class SparkweaveIntrinsicHolderTagsProvider<T> extends IntrinsicHolderTagsProvider<T> {
	/**
	 * @param modId the ID of the mod this generator belongs to
	 */
	@SuppressWarnings({"deprecation", "RedundantSuppression"}) // needed for NeoForge because of patched modId parameter
	public SparkweaveIntrinsicHolderTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<T>> parentProvider, Function<T, ResourceKey<T>> keyExtractor) {
		super(output, registryKey, lookupProvider, parentProvider, keyExtractor);
	}

	public SparkweaveIntrinsicHolderTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider, Function<T, ResourceKey<T>> keyExtractor) {
		this(output, registryKey, modId, lookupProvider, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()), keyExtractor);
	}

	/**
	 * @deprecated use {@link #tag(TagKey, String)} or {@link #existingTag(TagKey)}
	 */
	@Deprecated
	@Override
	protected TagAppender<T, T> tag(TagKey<T> tag) {
		return super.tag(tag);
	}

	/**
	 * helper function for when a tag already exists and therefore should not be translated
	 */
	protected TagAppender<T, T> existingTag(TagKey<T> tag) {
		return super.tag(tag);
	}

	/**
	 * @deprecated use {@link #getOrCreateRawBuilder(TagKey, String)} or {@link #getOrCreateExistingBuilder(TagKey)}
	 */
	@Deprecated
	@Override
	protected TagBuilder getOrCreateRawBuilder(TagKey<T> tag) {
		return super.getOrCreateRawBuilder(tag);
	}

	/**
	 * helper function for when a tag already exists and therefore should not be translated
	 */
	protected TagBuilder getOrCreateExistingBuilder(TagKey<T> tag) {
		return super.getOrCreateRawBuilder(tag);
	}

	protected TagBuilder getOrCreateRawBuilder(TagKey<T> tag, String tagName) {
		// TODO add tag translation to language file
		return super.getOrCreateRawBuilder(tag);
	}

	@SuppressWarnings("unchecked")
	protected TagAppender<T, T> tag(TagKey<T> tag, String tagName) {
		TagBuilder builder = this.getOrCreateRawBuilder(tag, tagName);
		return TagAppender.<T>forBuilder(builder).map(((IntrinsicHolderTagsProviderAcessor<T>) this).sparkweave$getKeyExtractor());
	}
}
