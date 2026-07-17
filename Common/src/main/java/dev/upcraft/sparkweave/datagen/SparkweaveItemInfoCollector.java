package dev.upcraft.sparkweave.datagen;

import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SparkweaveItemInfoCollector implements ItemModelOutput {
	private final Map<ResourceKey<Item>, ClientItem> itemInfos = new IdentityHashMap<>();
	private final Map<Item, Item> copies = new HashMap<>();
	private final Supplier<Stream<? extends Holder<Item>>> knownItems;

	public SparkweaveItemInfoCollector(Supplier<Stream<? extends Holder<Item>>> knownItems) {
		this.knownItems = knownItems;
	}

	public void accept(Item item, ItemModel.Unbaked model, ClientItem.Properties properties) {
		this.register(item, new ClientItem(model, properties));
	}

	@SuppressWarnings("deprecation")
	public void register(Item item, ClientItem itemInfo) {
		register(item.builtInRegistryHolder().key(), itemInfo);
	}

	public void register(Identifier identifier, ClientItem clientItem) {
		var key = ResourceKey.create(Registries.ITEM, identifier);
		register(key, clientItem);
	}

	public void register(ResourceKey<Item> id, ClientItem clientItem) {
		ClientItem existing = this.itemInfos.putIfAbsent(id, clientItem);
		if (existing != null) {
			throw new IllegalStateException("Duplicate item model definition for %s".formatted(id.identifier()));
		}
	}

	public void copy(Item donor, Item acceptor) {
		this.copies.put(acceptor, donor);
	}

	@SuppressWarnings("deprecation")
	public void finalizeAndValidate() {
		this.knownItems.get().map(Holder::value).forEach((item) -> {
			if (!this.copies.containsKey(item) && item instanceof BlockItem blockItem) {
				if (!this.itemInfos.containsKey(blockItem.builtInRegistryHolder().key())) {
					Identifier targetModel = ModelLocationUtils.getModelLocation(blockItem.getBlock());
					this.accept(blockItem, ItemModelUtils.plainModel(targetModel));
				}
			}
		});
		this.copies.forEach((acceptor, donor) -> {
			ClientItem donorInfo = this.itemInfos.get(donor);
			if (donorInfo == null) {
				String var10002 = String.valueOf(donor);
				throw new IllegalStateException("Missing donor: " + var10002 + " -> " + acceptor);
			} else {
				this.register(acceptor, donorInfo);
			}
		});
		List<Identifier> missingDefinitions = this.knownItems.get()
			.map(Holder::unwrapKey)
			.map(Optional::orElseThrow)
			.filter(Predicate.not(this.itemInfos::containsKey))
			.map(ResourceKey::identifier)
			.toList();
		if (!missingDefinitions.isEmpty()) {
			throw new IllegalStateException("Missing item model definitions for: " + missingDefinitions);
		}
	}

	public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
		Objects.requireNonNull(pathProvider);
		return DataProvider.saveAll(cache, ClientItem.CODEC, pathProvider::json, this.itemInfos);
	}
}
