package dev.upcraft.sparkweave.api.util.ext.datagen.fabric;

import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.world.level.block.Block;

public interface FabricModelProviderExt {

	default void copyModelNoItem(BlockModelGenerators gen, Block sourceBlock, Block targetBlock) {
		throw new UnsupportedOperationException();
	}
}
