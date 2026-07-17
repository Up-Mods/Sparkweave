package dev.upcraft.sparkweave.testmod.datagen;

import com.google.auto.service.AutoService;
import dev.upcraft.sparkweave.api.datagen.DataGenerationContext;
import dev.upcraft.sparkweave.api.datagen.DynamicRegistryBuilder;
import dev.upcraft.sparkweave.api.entrypoint.DataGenerationEntryPoint;
import dev.upcraft.sparkweave.testmod.datagen.client.TestmodEnglishLanguageProvider;
import dev.upcraft.sparkweave.testmod.datagen.common.*;

@AutoService(DataGenerationEntryPoint.class)
public class TestmodDataGenerator implements DataGenerationEntryPoint {

	@Override
	public void generateDynamicRegistryEntries(DynamicRegistryBuilder builder) {
		builder.add(TestmodBiomeProvider::new);
		builder.add(TestmodPlacedFeatureProvider::new);
		builder.add(TestmodConfiguredFeatureProvider::new);
		builder.add(TestmodEnchantmentProvider::new);
	}

	@Override
	public void generate(DataGenerationContext ctx) {
		var pack = ctx.getDefaultPack();
		pack.addProvider(TestmodItemTagsProvider::new);

		pack.addProvider(DataGenerationContext::includeClient, TestmodEnglishLanguageProvider::new);
		pack.addProvider(TestmodRecipeProvider::new);
	}
}
