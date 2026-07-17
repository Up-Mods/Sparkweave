package dev.upcraft.sparkweave.api.datagen.provider.client;

import dev.upcraft.sparkweave.api.datagen.ContextAwarePackOutput;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.datagen.SparkweaveBlockStateGeneratorCollector;
import dev.upcraft.sparkweave.datagen.SparkweaveItemInfoCollector;
import dev.upcraft.sparkweave.datagen.SparkweaveSimpleModelCollector;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public abstract class SparkweaveModelProvider implements DataProvider {

	private final PackOutput.PathProvider blockStatePathProvider;
	private final PackOutput.PathProvider itemInfoPathProvider;
	private final PackOutput.PathProvider modelPathProvider;
	private final ModContainer modContainer;

	public SparkweaveModelProvider(ContextAwarePackOutput output) {
		this.blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
		this.itemInfoPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
		this.modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
		this.modContainer = output.getModContainer();
	}

	protected abstract void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels);

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		var itemModels = new SparkweaveItemInfoCollector(this::getKnownItems);
		var blockStateGenerators = new SparkweaveBlockStateGeneratorCollector(this::getKnownBlocks);
		var simpleModels = new SparkweaveSimpleModelCollector();
		this.registerModels(new BlockModelGenerators(blockStateGenerators, itemModels, simpleModels), new ItemModelGenerators(itemModels, simpleModels));
		blockStateGenerators.validate();
		itemModels.finalizeAndValidate();
		return CompletableFuture.allOf(
			blockStateGenerators.save(cache, this.blockStatePathProvider),
			simpleModels.save(cache, this.modelPathProvider),
			itemModels.save(cache, this.itemInfoPathProvider)
		);
	}

	@Override
	public String getName() {
		return "%s::Models".formatted(modContainer.metadata().displayName());
	}

	protected Stream<? extends Holder<Block>> getKnownBlocks() {
		var modId = modContainer.metadata().id();
		return BuiltInRegistries.BLOCK.listElements().filter((holder) -> holder.key().identifier().getNamespace().equals(modId));
	}

	protected Stream<? extends Holder<Item>> getKnownItems() {
		var modId = modContainer.metadata().id();
		return BuiltInRegistries.ITEM.listElements().filter((holder) -> holder.key().identifier().getNamespace().equals(modId));
	}
}
