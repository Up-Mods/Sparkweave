package dev.upcraft.sparkweave.api.block;

import dev.upcraft.sparkweave.api.registry.block.InjectIntoBlockEntity;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class SparkweaveWallSignBlock extends WallSignBlock implements InjectIntoBlockEntity {

	public SparkweaveWallSignBlock(WoodType type, Properties properties) {
		super(type, properties);
	}

	@Override
	public BlockEntityType<?> getBlockEntityTypeToInjectInto() {
		return BlockEntityType.SIGN;
	}
}
