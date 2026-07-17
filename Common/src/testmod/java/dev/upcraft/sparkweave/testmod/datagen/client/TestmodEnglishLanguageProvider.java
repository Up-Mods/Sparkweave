package dev.upcraft.sparkweave.testmod.datagen.client;

import dev.upcraft.sparkweave.api.datagen.ContextAwarePackOutput;
import dev.upcraft.sparkweave.api.datagen.TranslationBuilder;
import dev.upcraft.sparkweave.api.datagen.provider.client.SparkweaveLanguageProvider;
import dev.upcraft.sparkweave.testmod.init.TestCreativeTabs;
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
		builder.item(TestItems.TEST_ITEM, "Test Item");

		builder.item(TestItems.MAGE_HOOD, "Mage Hood");
		builder.item(TestItems.MAGE_ROBES, "Mage Robes");
		builder.item(TestItems.MAGE_LEGGINGS, "Mage Leggings");
		builder.item(TestItems.MAGE_BOOTS, "Mage Boots");
	}
}
