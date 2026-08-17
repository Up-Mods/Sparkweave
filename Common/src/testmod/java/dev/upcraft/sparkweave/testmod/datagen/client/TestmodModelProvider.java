package dev.upcraft.sparkweave.testmod.datagen.client;

import dev.upcraft.sparkweave.api.datagen.ContextAwarePackOutput;
import dev.upcraft.sparkweave.api.datagen.provider.client.SparkweaveModelProvider;
import dev.upcraft.sparkweave.testmod.data.TestmodEquipmentAssets;
import dev.upcraft.sparkweave.testmod.init.TestBlocks;
import dev.upcraft.sparkweave.testmod.init.TestItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

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

		blockModels.createTrivialCube(TestBlocks.TEST_BLOCK.get());
	}
}
