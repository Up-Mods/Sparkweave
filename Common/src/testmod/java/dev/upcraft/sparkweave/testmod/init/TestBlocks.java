package dev.upcraft.sparkweave.testmod.init;

import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import dev.upcraft.sparkweave.api.registry.block.BlockRegistryHandler;
import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import dev.upcraft.sparkweave.testmod.block.TestBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class TestBlocks {
	public static final BlockRegistryHandler BLOCKS = RegistryHandler.blocks(SparkweaveTestmod.MODID);

	public static final RegistrySupplier<TestBlock> TEST_BLOCK = BLOCKS.register("test_block", TestBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));
}
