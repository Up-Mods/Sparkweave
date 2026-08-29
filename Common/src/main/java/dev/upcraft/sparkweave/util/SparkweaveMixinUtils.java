package dev.upcraft.sparkweave.util;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;

public class SparkweaveMixinUtils {
	public static final ThreadLocal<BlockEntityType<? extends HangingSignBlockEntity>> HANGING_SIGN_BE_TYPE = new ThreadLocal<>();
}
