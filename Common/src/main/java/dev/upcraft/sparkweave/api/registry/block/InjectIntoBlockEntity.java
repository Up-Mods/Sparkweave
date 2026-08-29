package dev.upcraft.sparkweave.api.registry.block;

import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Collection;
import java.util.Set;

public interface InjectIntoBlockEntity {

	default Collection<BlockEntityType<?>> getBlockEntityTypesToInjectInto() {
		return Set.of(getBlockEntityTypeToInjectInto());
	}

	BlockEntityType<?> getBlockEntityTypeToInjectInto();
}
