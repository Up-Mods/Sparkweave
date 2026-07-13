package dev.upcraft.sparkweave.registry.block;

import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.block.BlockRegistryHandler;
import dev.upcraft.sparkweave.registry.IdAwareRegistryHandlerImpl;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockRegistryHandlerImpl extends IdAwareRegistryHandlerImpl<Block, BlockBehaviour.Properties> implements BlockRegistryHandler {

	public BlockRegistryHandlerImpl(RegistryHandler<Block> delegate) {
		super(delegate, BlockBehaviour.Properties::setId);
	}
}
