package dev.upcraft.sparkweave.testmod.datagen;

import dev.upcraft.sparkweave.api.datagen.DataGenerationContext;
import dev.upcraft.sparkweave.api.datagen.DynamicRegistryBuilder;
import dev.upcraft.sparkweave.api.entrypoint.DataGenerationEntryPoint;
import dev.upcraft.sparkweave.testmod.datagen.client.TestmodEnglishLanguageProvider;
import dev.upcraft.sparkweave.testmod.datagen.common.TestmodBiomeProvider;
import dev.upcraft.sparkweave.testmod.datagen.common.TestmodEnchantmentProvider;

public class TestmodDataGenerator implements DataGenerationEntryPoint {

	@Override
	public void generateDynamicRegistryEntries(DynamicRegistryBuilder builder) {
		builder.add(TestmodBiomeProvider::new);
		builder.add(TestmodEnchantmentProvider::new);
	}

	@Override
	public void generate(DataGenerationContext ctx) {
		var pack = ctx.getDefaultPack();
		pack.addProvider(DataGenerationContext::includeClient, TestmodEnglishLanguageProvider::new);
	}
}
