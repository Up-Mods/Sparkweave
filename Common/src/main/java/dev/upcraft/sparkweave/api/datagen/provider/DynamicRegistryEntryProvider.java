package dev.upcraft.sparkweave.api.datagen.provider;

import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class DynamicRegistryEntryProvider {

	private final Map<String, String> storedTranslations = new TreeMap<>();
	protected final Logger LOGGER = SparkweaveLoggerFactory.getLogger();

	public abstract void generate(RegistrySetBuilder builder);

	public abstract String getName();

	public CompletableFuture<?> generateEntryData(CachedOutput output, HolderLookup.Provider registries) {
		return CompletableFuture.completedFuture(null);
	}

	protected void addTranslation(String key, String translation) {
		storedTranslations.put(key, translation);
	}

	public void appendTranslations(BiConsumer<String, String> translationsBuilder) {
		storedTranslations.forEach(translationsBuilder);
	}

	protected abstract static class Context<T> {

		protected final BootstrapContext<T> bootstrapContext;

		protected Context(BootstrapContext<T> bootstrapContext) {
			this.bootstrapContext = bootstrapContext;
		}

		public <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> registryKey) {
			return bootstrapContext.lookup(registryKey);
		}
	}
}
