package dev.upcraft.sparkweave.testmod.datagen.client;

import dev.upcraft.sparkweave.api.datagen.ContextAwarePackOutput;
import dev.upcraft.sparkweave.api.datagen.provider.client.SparkweaveModelProvider;
import dev.upcraft.sparkweave.testmod.block.BerryBushBlock;
import dev.upcraft.sparkweave.testmod.data.TestmodBlockFamilies;
import dev.upcraft.sparkweave.testmod.data.TestmodEquipmentAssets;
import dev.upcraft.sparkweave.testmod.init.TestBlocks;
import dev.upcraft.sparkweave.testmod.init.TestItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class TestmodModelProvider extends SparkweaveModelProvider {

	public TestmodModelProvider(ContextAwarePackOutput output) {
		super(output);
	}

	@Override
	protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
		itemModels.generateFlatItem(TestItems.TEST_ITEM.get(), ModelTemplates.FLAT_ITEM);

		itemModels.generateTrimmableItem(TestItems.MAGE_HOOD.get(), TestmodEquipmentAssets.MAGE_ROBES, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
		itemModels.generateTrimmableItem(TestItems.MAGE_ROBES.get(), TestmodEquipmentAssets.MAGE_ROBES, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
		itemModels.generateTrimmableItem(TestItems.MAGE_LEGGINGS.get(), TestmodEquipmentAssets.MAGE_ROBES, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
		itemModels.generateTrimmableItem(TestItems.MAGE_BOOTS.get(), TestmodEquipmentAssets.MAGE_ROBES, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);

		createBerryBush(blockModels, itemModels, TestBlocks.BLUEBERRY_BUSH, TestItems.BLUEBERRY);

		createFamily(blockModels, TestmodBlockFamilies.TEST);
		blockModels.createHangingSign(TestBlocks.TEST_BLOCK.get(), TestBlocks.TEST_HANGING_SIGN.get(), TestBlocks.TEST_WALL_HANGING_SIGN.get());
		itemModels.generateFlatItem(TestItems.TEST_BOAT.get(), ModelTemplates.FLAT_ITEM);
	}

	private static void createFamily(BlockModelGenerators blockModels, BlockFamily family) {
		blockModels.family(family.getBaseBlock()).generateFor(family);
	}

	private void createBerryBush(BlockModelGenerators blockModels, ItemModelGenerators itemModels, Supplier<? extends BerryBushBlock> block, Supplier<Item> item) {
		itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM);

		var actualBlock = block.get();
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(actualBlock)
			.with(PropertyDispatch.initial(BerryBushBlock.AGE)
				.generate(age -> BlockModelGenerators.plainVariant(
					blockModels.createSuffixedVariant(actualBlock, "_stage_" + age, ModelTemplates.CROSS, TextureMapping::cross)
				))
			)
		);
	}
}
