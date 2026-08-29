package dev.upcraft.sparkweave.mixin.customsigns;

import dev.upcraft.sparkweave.util.SparkweaveMixinUtils;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HangingSignBlockEntity.class)
public abstract class HangingSignBlockEntityMixin {

	@SuppressWarnings("unchecked")
	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/SignBlockEntity;<init>(Lnet/minecraft/world/level/block/entity/BlockEntityType;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"), index = 0)
	private static BlockEntityType<HangingSignBlockEntity> getBeType(BlockEntityType<HangingSignBlockEntity> original) {
		var value = SparkweaveMixinUtils.HANGING_SIGN_BE_TYPE.get();
		if(value != null) {
			SparkweaveMixinUtils.HANGING_SIGN_BE_TYPE.remove();
			return (BlockEntityType<HangingSignBlockEntity>) value;
		}

		return original;
	}
}
