package dev.upcraft.sparkweave.neoforge.impl.datagen;

import dev.upcraft.sparkweave.api.datagen.ContextAwarePackOutput;
import dev.upcraft.sparkweave.api.datagen.DataGenerationContext;
import dev.upcraft.sparkweave.api.datagen.Pack;
import dev.upcraft.sparkweave.api.datagen.provider.client.SparkweaveLanguageProvider;
import dev.upcraft.sparkweave.api.datagen.provider.common.dynamic.SparkweaveDynamicRegistryEntryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

public class NeoBuiltinPack implements Pack {

	private final NeoDataGenerationContext context;
	private final DataGenerator rootGenerator;
	private final CompletableFuture<HolderLookup.Provider> registriesFuture;
	private final List<SparkweaveDynamicRegistryEntryProvider> dynamicProviders;
	private final ContextAwarePackOutput output;
	private boolean hasTranslations;

	public NeoBuiltinPack(NeoDataGenerationContext context, DataGenerator rootGenerator, CompletableFuture<HolderLookup.Provider> registriesFuture, List<SparkweaveDynamicRegistryEntryProvider> dynamicProviders) {
		this.context = context;
		this.rootGenerator = rootGenerator;
		this.registriesFuture = registriesFuture;
		this.dynamicProviders = dynamicProviders;
		this.output = new ContextAwarePackOutput(rootGenerator.getPackOutput().getOutputFolder(), context.getMod());
	}

	@Override
	public <T extends DataProvider> T addProvider(Predicate<DataGenerationContext> enabled, Function<ContextAwarePackOutput, T> factory) {
		T provider = rootGenerator.addProvider(enabled.test(context), factory.apply(output));

		if(!hasTranslations && provider instanceof SparkweaveLanguageProvider languageProvider && languageProvider.isDefaultLanguage()) {
			dynamicProviders.forEach(it -> it.appendTranslations(languageProvider::addExtra));
			hasTranslations = true;
		}

		return provider;
	}

	@Override
	public <T extends DataProvider> T addProvider(Predicate<DataGenerationContext> enabled, RegistryDependentFactory<T> factory) {
		T provider = rootGenerator.addProvider(enabled.test(context), factory.create(output, registriesFuture));

		if(!hasTranslations && provider instanceof SparkweaveLanguageProvider languageProvider && languageProvider.isDefaultLanguage()) {
			dynamicProviders.forEach(it -> it.appendTranslations(languageProvider::addExtra));
			hasTranslations = true;
		}

		return provider;
	}

	@Override
	public <T extends DataProvider> T addProvider(Function<ContextAwarePackOutput, T> factory) {
		return addProvider(_ -> true, factory);
	}

	@Override
	public <T extends DataProvider> T addProvider(RegistryDependentFactory<T> factory) {
		return addProvider(_ -> true, factory);
	}
}
