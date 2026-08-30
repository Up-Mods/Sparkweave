package dev.upcraft.sparkweave.testmod.datagen.client;

import dev.upcraft.sparkweave.api.datagen.ContextAwarePackOutput;
import dev.upcraft.sparkweave.api.datagen.TranslationBuilder;
import dev.upcraft.sparkweave.api.datagen.provider.client.SparkweaveLanguageProvider;
import dev.upcraft.sparkweave.testmod.data.TestmodTags;
import dev.upcraft.sparkweave.testmod.init.TestBlocks;
import dev.upcraft.sparkweave.testmod.init.TestCreativeTabs;
import dev.upcraft.sparkweave.testmod.init.TestEntities;
import dev.upcraft.sparkweave.testmod.init.TestItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.locale.Language;

import java.util.concurrent.CompletableFuture;

public class TestmodEnglishLanguageProvider extends SparkweaveLanguageProvider {

	public TestmodEnglishLanguageProvider(ContextAwarePackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture, Language.DEFAULT);
	}

	@Override
	public void generateTranslations(HolderLookup.Provider registries, TranslationBuilder builder) {
		builder.creativeTab(TestCreativeTabs.ITEMS, "TestMod Items");

		builder.block(TestBlocks.TEST_BLOCK, "Test Block");
		builder.block(TestBlocks.TEST_STAIRS, "Test Stairs");
		builder.item(TestItems.TEST_SIGN, "Test Sign");
		builder.item(TestItems.TEST_HANGING_SIGN, "Test Hanging Sign");

		builder.item(TestItems.TEST_ITEM, "Test Item");

		builder.block(TestBlocks.BLUEBERRY_BUSH, "Blueberry Bush");
		builder.item(TestItems.BLUEBERRY, "Blueberry");

		builder.item(TestItems.MAGE_HOOD, "Mage Hood");
		builder.item(TestItems.MAGE_ROBES, "Mage Robes");
		builder.item(TestItems.MAGE_LEGGINGS, "Mage Leggings");
		builder.item(TestItems.MAGE_BOOTS, "Mage Boots");

		builder.item(TestItems.TEST_BOAT, "Test Boat");
		builder.entity(TestEntities.TEST_BOAT, "Test Boat");

		// TODO remove once implemented in tag provider
		builder.tag(TestmodTags.Items.MAGE_ROBES_REPAIR_MATERIALS, "Mage Robes repair materials");
	}
}
