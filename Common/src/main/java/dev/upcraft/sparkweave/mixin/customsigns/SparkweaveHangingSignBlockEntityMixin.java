package dev.upcraft.sparkweave.mixin.customsigns;

import dev.upcraft.sparkweave.api.blockentity.SparkweaveHangingSignBlockEntity;
import dev.upcraft.sparkweave.util.SparkweaveMixinUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SparkweaveHangingSignBlockEntity.class)
public abstract class SparkweaveHangingSignBlockEntityMixin {

	@Inject(method = "<init>(Lnet/minecraft/world/level/block/entity/BlockEntityType;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/HangingSignBlockEntity;<init>(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"))
	private static void ctorHead(BlockEntityType<? extends SparkweaveHangingSignBlockEntity> type, BlockPos worldPosition, BlockState blockState, CallbackInfo ci) {
		SparkweaveMixinUtils.HANGING_SIGN_BE_TYPE.set(type);
	}

	@Inject(method = "<init>(Lnet/minecraft/world/level/block/entity/BlockEntityType;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/HangingSignBlockEntity;<init>(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", shift = At.Shift.AFTER))
	private static void ctorTail(BlockEntityType<? extends SparkweaveHangingSignBlockEntity> type, BlockPos worldPosition, BlockState blockState, CallbackInfo ci) {
		SparkweaveMixinUtils.HANGING_SIGN_BE_TYPE.remove();
	}
}
