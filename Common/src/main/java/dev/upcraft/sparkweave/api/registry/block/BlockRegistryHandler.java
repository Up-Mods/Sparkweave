package dev.upcraft.sparkweave.api.registry.block;

import dev.upcraft.sparkweave.api.registry.IdAwareRegistryHandler;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public interface BlockRegistryHandler extends IdAwareRegistryHandler<Block, BlockBehaviour.Properties> {
}
