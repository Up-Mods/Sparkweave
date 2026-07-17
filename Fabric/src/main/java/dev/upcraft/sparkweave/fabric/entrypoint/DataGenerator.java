package dev.upcraft.sparkweave.fabric.entrypoint;

import dev.upcraft.sparkweave.api.datagen.DataGenerationContext;
import dev.upcraft.sparkweave.api.datagen.provider.common.dynamic.SparkweaveDynamicRegistryEntryProvider;
import dev.upcraft.sparkweave.api.entrypoint.DataGenerationEntryPoint;
import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.datagen.DynamicRegistryBuilderImpl;
import dev.upcraft.sparkweave.datagen.SparkweaveDatagenHelper;
import dev.upcraft.sparkweave.entrypoint.EntrypointHelper;
import dev.upcraft.sparkweave.fabric.impl.datagen.FabricBuiltinEntriesProvider;
import dev.upcraft.sparkweave.fabric.impl.datagen.FabricDataGenerationContext;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataGenerator implements DataGeneratorEntrypoint {

	private final Map<ModContainer, List<SparkweaveDynamicRegistryEntryProvider>> dynamicProviders = Collections.synchronizedMap(new LinkedHashMap<>());
	private RegistrySetBuilder registrySetBuilder;

	private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		this.registrySetBuilder = registryBuilder;

		EntrypointHelper.fireEntrypoints(DataGenerationEntryPoint.class, (dataGenerator, contextModContainer) -> {
			if (SparkweaveDatagenHelper.ENABLED_MODS.contains(contextModContainer.metadata().id())) {
				LOGGER.info("Gathering dynamic registry data for {}", contextModContainer.metadata().displayName());
				var dynamicRegistryBuilder = new DynamicRegistryBuilderImpl(contextModContainer, registryBuilder);
				dataGenerator.generateDynamicRegistryEntries(dynamicRegistryBuilder);
				dynamicProviders.put(contextModContainer, dynamicRegistryBuilder.getProviders());
			}
		});
	}

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricGenerator) {
		var registriesFuture = fabricGenerator.getRegistries();

		// need to wait here so all the data from buildRegistry() is ready before we proceed!
		registriesFuture.join();

		var defaultPack = fabricGenerator.createPack();
		var strictValidation = fabricGenerator.isStrictValidationEnabled();

		defaultPack.addProvider((output, registriesFuture1) -> new FabricBuiltinEntriesProvider(output, registriesFuture1, registrySetBuilder, SparkweaveDatagenHelper.ENABLED_MODS, dynamicProviders));

		EntrypointHelper.fireEntrypoints(DataGenerationEntryPoint.class, (dataGenerator, contextModContainer) -> {
			if (SparkweaveDatagenHelper.ENABLED_MODS.contains(contextModContainer.metadata().id())) {
				LOGGER.info("Generating data for {}", contextModContainer.metadata().displayName());
				DataGenerationContext ctx = new FabricDataGenerationContext(contextModContainer, strictValidation, defaultPack, registriesFuture, dynamicProviders.get(contextModContainer));
				dataGenerator.generate(ctx);
			}
		});
	}
}
