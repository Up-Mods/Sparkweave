package dev.upcraft.sparkweave.testmod.init;

import dev.upcraft.sparkweave.api.block.SparkweaveCeilingHangingSignBlock;
import dev.upcraft.sparkweave.api.block.SparkweaveStandingSignBlock;
import dev.upcraft.sparkweave.api.block.SparkweaveWallHangingSignBlock;
import dev.upcraft.sparkweave.api.block.SparkweaveWallSignBlock;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import dev.upcraft.sparkweave.api.registry.block.BlockRegistryHandler;
import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import dev.upcraft.sparkweave.testmod.block.BerryBushBlock;
import dev.upcraft.sparkweave.testmod.block.BlueberryBushBlock;
import dev.upcraft.sparkweave.testmod.block.TestBlock;
import dev.upcraft.sparkweave.testmod.block.TestStairBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;

public class TestBlocks {
	public static final BlockRegistryHandler BLOCKS = RegistryHandler.blocks(SparkweaveTestmod.MODID);

	public static final RegistrySupplier<TestBlock> TEST_BLOCK = BLOCKS.register("test_block", TestBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));
	public static final RegistrySupplier<TestStairBlock> TEST_STAIRS = BLOCKS.register("test_stairs", properties -> new TestStairBlock(TEST_BLOCK.get().defaultBlockState(), properties), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_STAIRS));

	public static final RegistrySupplier<SparkweaveStandingSignBlock> TEST_SIGN = BLOCKS.register("test_sign", properties -> new SparkweaveStandingSignBlock(WoodType.OAK, properties), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN));
	public static final RegistrySupplier<SparkweaveWallSignBlock> TEST_WALL_SIGN = BLOCKS.register("test_wall_sign", properties -> new SparkweaveWallSignBlock(WoodType.OAK, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).setOverridesFrom(TestBlocks.TEST_SIGN));
	public static final RegistrySupplier<SparkweaveCeilingHangingSignBlock> TEST_HANGING_SIGN = BLOCKS.register("test_hanging_sign", properties -> new SparkweaveCeilingHangingSignBlock(WoodType.OAK, properties), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN));
	public static final RegistrySupplier<SparkweaveWallHangingSignBlock> TEST_WALL_HANGING_SIGN = BLOCKS.register("test_wall_hanging_sign", properties -> new SparkweaveWallHangingSignBlock(WoodType.OAK, properties), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN).setOverridesFrom(TestBlocks.TEST_HANGING_SIGN));

	public static final RegistrySupplier<BerryBushBlock> BLUEBERRY_BUSH = BLOCKS.register("blueberry_bush", BlueberryBushBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH));
}
