package dev.upcraft.sparkweave.fabric.impl.datagen;

import dev.upcraft.sparkweave.api.datagen.ContextAwarePackOutput;
import dev.upcraft.sparkweave.api.datagen.DataGenerationContext;
import dev.upcraft.sparkweave.api.datagen.Pack;
import dev.upcraft.sparkweave.api.datagen.provider.SparkweaveDynamicRegistryEntryProvider;
import dev.upcraft.sparkweave.api.datagen.provider.SparkweaveLanguageProvider;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.fabric.mixin.datagen.PackGeneratorAccessor;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

public class FabricBuiltinPack implements Pack {

	private final FabricDataGenerationContext context;
	private final FabricDataGenerator.Pack pack;
	private final CompletableFuture<HolderLookup.Provider> registriesFuture;
	private final List<SparkweaveDynamicRegistryEntryProvider> dynamicProviders;
	private final ContextAwarePackOutput output;
	private boolean hasTranslations;

	public FabricBuiltinPack(FabricDataGenerationContext context, FabricDataGenerator.Pack pack, CompletableFuture<HolderLookup.Provider> registriesFuture, List<SparkweaveDynamicRegistryEntryProvider> dynamicProviders) {
		this.context = context;
		this.pack = pack;
		this.registriesFuture = registriesFuture;
		this.dynamicProviders = dynamicProviders;
		this.output = new ContextAwarePackOutput(((PackGeneratorAccessor) (Object) pack).sparkweave$getPackOutput().getOutputFolder(), context.getMod());
	}

	@Override
	public ModContainer getOwner() {
		return context.getMod();
	}

	@Override
	public <T extends DataProvider> T addProvider(Predicate<DataGenerationContext> enabled, Function<ContextAwarePackOutput, T> factory) {
		T provider = factory.apply(output);

		if (enabled.test(context)) {
			pack.addProvider((FabricPackOutput fabricOutput) -> provider);
		}

		if(!hasTranslations && provider instanceof SparkweaveLanguageProvider languageProvider && languageProvider.isDefaultLanguage()) {
			dynamicProviders.forEach(it -> it.appendTranslations(languageProvider::addExtra));
			hasTranslations = true;
		}

		return provider;
	}

	@Override
	public <T extends DataProvider> T addProvider(Predicate<DataGenerationContext> enabled, RegistryDependentFactory<T> factory) {
		T provider = factory.create(output, registriesFuture);

		if (enabled.test(context)) {
			pack.addProvider((fabricOutput, registriesFuture1) -> provider);
		}

		if(!hasTranslations && provider instanceof SparkweaveLanguageProvider languageProvider && languageProvider.isDefaultLanguage()) {
			dynamicProviders.forEach(it -> it.appendTranslations(languageProvider::addExtra));
			hasTranslations = true;
		}

		return provider;
	}
}
