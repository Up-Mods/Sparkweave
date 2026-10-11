package dev.upcraft.sparkweave.testmod.datagen.common.loot;

import dev.upcraft.sparkweave.testmod.init.TestBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;

import java.util.Set;

public class TestmodBlockLoot extends BlockLootSubProvider {

	public TestmodBlockLoot(HolderLookup.Provider registries) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
	}

	@Override
	public void generate() {
		dropSelf(TestBlocks.TEST_BLOCK.get());
		dropSelf(TestBlocks.TEST_STAIRS.get());
		dropSelf(TestBlocks.TEST_SIGN.get());
		dropSelf(TestBlocks.TEST_HANGING_SIGN.get());
		dropSelf(TestBlocks.BLUEBERRY_BUSH.get());
		dropSelf(TestBlocks.TEST_FLOWER.get());
		dropPottedContents(TestBlocks.POTTED_TEST_FLOWER.get());
	}
}
