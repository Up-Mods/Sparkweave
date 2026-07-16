package dev.upcraft.sparkweave.api.datagen.provider;

import dev.upcraft.sparkweave.mixin.datagen.IntrinsicHolderTagsProviderAcessor;
import dev.upcraft.sparkweave.mixin.datagen.TagsProviderAccessor;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@ApiStatus.Experimental
public abstract class SparkweaveItemTagProvider extends SparkweaveIntrinsicHolderTagsProvider<Item> {

	@Nullable
	private final Function<TagKey<Block>, TagBuilder> blockTagBuilderProvider;

	@SuppressWarnings({"deprecation", "unchecked"})
	public SparkweaveItemTagProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Item>> parentProvider, @Nullable IntrinsicHolderTagsProvider<Block> blockTagsProvider) {
		super(output, Registries.ITEM, modId, lookupProvider, parentProvider, item -> item.builtInRegistryHolder().key());
		this.blockTagBuilderProvider = blockTagsProvider != null ? ((TagsProviderAccessor<Block>) blockTagsProvider)::sparkweave$getOrCreateRawBuilder : null;
	}

	public SparkweaveItemTagProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable IntrinsicHolderTagsProvider<Block> blockTagBuilderProvider) {
		this(output, modId, lookupProvider, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()), blockTagBuilderProvider);
	}

	public SparkweaveItemTagProvider(PackOutput output, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		this(output, modId, lookupProvider, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()), null);
	}

	@SuppressWarnings("unchecked")
	protected TagAppender<Item, Item> copyBlockTag(TagKey<Block> blockTag, TagKey<Item> itemTag, String itemTagName) {
		var blockTags = Objects.requireNonNull(this.blockTagBuilderProvider, "Pass Block tags provider via constructor to use copy").apply(blockTag).build();

		var builder = getOrCreateRawBuilder(itemTag, itemTagName);
		blockTags.forEach(builder::add);

		return TagAppender.<Item>forBuilder(builder).map(((IntrinsicHolderTagsProviderAcessor<Item>) this).sparkweave$getKeyExtractor());
	}
}
