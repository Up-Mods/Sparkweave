package dev.upcraft.sparkweave.datagen;

import dev.upcraft.sparkweave.api.datagen.DynamicRegistryBuilder;
import dev.upcraft.sparkweave.api.datagen.provider.DynamicRegistryEntryProvider;
import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.core.RegistrySetBuilder;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class DynamicRegistryBuilderImpl implements DynamicRegistryBuilder {

	private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();

	private final List<DynamicRegistryEntryProvider> providers = new ArrayList<>();
	private final ModContainer modContainer;
	private final RegistrySetBuilder registrySetBuilder;

	public DynamicRegistryBuilderImpl(ModContainer modContainer, RegistrySetBuilder registrySetBuilder) {
		this.modContainer = modContainer;
		this.registrySetBuilder = registrySetBuilder;
	}

	@Override
	public DynamicRegistryBuilder add(Supplier<DynamicRegistryEntryProvider> factory) {
		var provider = factory.get();
		LOGGER.info("Collecting entries: {}/{}", modContainer.metadata().displayName(), provider.getName());
		provider.generate(registrySetBuilder);
		providers.add(provider);
		return this;
	}

	public List<DynamicRegistryEntryProvider> getProviders() {
		return Collections.unmodifiableList(providers);
	}
}
