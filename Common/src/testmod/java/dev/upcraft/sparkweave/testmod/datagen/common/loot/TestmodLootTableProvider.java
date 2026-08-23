package dev.upcraft.sparkweave.testmod.datagen.common.loot;

import dev.upcraft.sparkweave.testmod.data.TestmodLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TestmodLootTableProvider extends LootTableProvider {

	public TestmodLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, TestmodLootTables.all(), List.of(
			new SubProviderEntry(TestmodBlockInteractLoot::new, LootContextParamSets.BLOCK_INTERACT)
		), registries);
	}
}
