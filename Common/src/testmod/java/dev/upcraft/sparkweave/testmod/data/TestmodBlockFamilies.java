package dev.upcraft.sparkweave.testmod.data;

import dev.upcraft.sparkweave.testmod.init.TestBlocks;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;

public class TestmodBlockFamilies {

	public static final BlockFamily TEST = BlockFamilies.familyBuilder(TestBlocks.TEST_BLOCK.get())
		.stairs(TestBlocks.TEST_STAIRS.get())
		.sign(TestBlocks.TEST_SIGN.get(), TestBlocks.TEST_WALL_SIGN.get())
		.getFamily();
}
