package dev.upcraft.sparkweave.testmod.block;

import dev.upcraft.sparkweave.api.registry.block.BlockItemProvider;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TestStairBlock extends StairBlock implements BlockItemProvider {

	public TestStairBlock(BlockState baseState, Properties properties) {
		super(baseState, properties);
	}
}
