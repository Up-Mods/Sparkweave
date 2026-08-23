package dev.upcraft.sparkweave.testmod.block;

import dev.upcraft.sparkweave.testmod.data.TestFoods;
import dev.upcraft.sparkweave.testmod.data.TestmodLootTables;
import net.minecraft.world.damagesource.DamageTypes;

public class BlueberryBushBlock extends BerryBushBlock {

	public BlueberryBushBlock(Properties properties) {
		super(properties, itemProps -> itemProps.food(TestFoods.BLUEBERRIES), TestmodLootTables.BLUEBERRY_BUSH_HARVEST, DamageTypes.SWEET_BERRY_BUSH, simpleCodec(BlueberryBushBlock::new));
	}
}
