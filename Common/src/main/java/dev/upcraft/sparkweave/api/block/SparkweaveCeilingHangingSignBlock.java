package dev.upcraft.sparkweave.api.block;

import dev.upcraft.sparkweave.api.registry.block.InjectIntoBlockEntity;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class SparkweaveCeilingHangingSignBlock extends CeilingHangingSignBlock implements InjectIntoBlockEntity {

	public SparkweaveCeilingHangingSignBlock(WoodType type, Properties properties) {
		super(type, properties);
	}

	@Override
	public BlockEntityType<?> getBlockEntityTypeToInjectInto() {
		return BlockEntityType.HANGING_SIGN;
	}
}
