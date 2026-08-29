package dev.upcraft.sparkweave.api.blockentity;

import dev.upcraft.sparkweave.mixin.customsigns.HangingSignBlockEntityMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/// Base class for custom [HangingSignBlockEntity], implemented via mixin hacks
/// @see HangingSignBlockEntityMixin
public class SparkweaveHangingSignBlockEntity extends HangingSignBlockEntity {

	public SparkweaveHangingSignBlockEntity(@SuppressWarnings("unused") BlockEntityType<? extends SparkweaveHangingSignBlockEntity> type, BlockPos worldPosition, BlockState blockState) {
		super(worldPosition, blockState);
	}
}
