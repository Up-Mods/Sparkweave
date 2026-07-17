package dev.upcraft.sparkweave.datagen;

import com.google.common.collect.Maps;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.minecraft.core.Holder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SparkweaveBlockStateGeneratorCollector implements Consumer<BlockModelDefinitionGenerator> {
	private final Map<Block, BlockModelDefinitionGenerator> generators = new HashMap<>();
	private final Supplier<Stream<? extends Holder<Block>>> knownBlocks;

	public SparkweaveBlockStateGeneratorCollector(Supplier<Stream<? extends Holder<Block>>> knownBlocks) {
		this.knownBlocks = knownBlocks;
	}

	public void accept(BlockModelDefinitionGenerator generator) {
		Block block = generator.block();
		BlockModelDefinitionGenerator prev = this.generators.put(block, generator);
		if (prev != null) {
			throw new IllegalStateException("Duplicate blockstate definition for " + block);
		}
	}

	public void validate() {
		List<Identifier> missingDefinitions = this.knownBlocks.get().filter((e) -> !this.generators.containsKey(e.value())).map((e) -> e.unwrapKey().orElseThrow().identifier()).toList();
		if (!missingDefinitions.isEmpty()) {
			throw new IllegalStateException("Missing blockstate definitions for: " + missingDefinitions);
		}
	}

	@SuppressWarnings("deprecation")
	public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
		Map<Block, BlockStateModelDispatcher> definitions = Maps.transformValues(this.generators, BlockModelDefinitionGenerator::create);
		Function<Block, Path> pathGetter = (block) -> pathProvider.json(block.builtInRegistryHolder().key().identifier());
		return DataProvider.saveAll(cache, BlockStateModelDispatcher.CODEC, pathGetter, definitions);
	}
}
