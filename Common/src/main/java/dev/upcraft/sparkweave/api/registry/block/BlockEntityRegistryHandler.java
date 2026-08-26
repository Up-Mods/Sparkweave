package dev.upcraft.sparkweave.api.registry.block;

import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;
import java.util.function.Supplier;

public interface BlockEntityRegistryHandler extends RegistryHandler<BlockEntityType<?>> {

	<T extends BlockEntity> RegistrySupplier<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Set<? extends Supplier<? extends Block>> allowedBlocks);
}
