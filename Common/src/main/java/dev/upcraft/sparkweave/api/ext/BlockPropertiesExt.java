package dev.upcraft.sparkweave.api.ext;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public interface BlockPropertiesExt {

	default BlockBehaviour.Properties setOverridesFrom(Supplier<? extends Block> parent) {
		throw new AssertionError("Implemented in Mixin");
	}
}
