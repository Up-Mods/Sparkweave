package dev.upcraft.sparkweave.testmod.block;

import dev.upcraft.sparkweave.api.registry.block.BlockItemProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

/// Test creation of default [BlockItem] in [BlockItemProvider]
public class TestBlock extends Block implements BlockItemProvider {
	public TestBlock(Properties properties) {
		super(properties);
	}
}
