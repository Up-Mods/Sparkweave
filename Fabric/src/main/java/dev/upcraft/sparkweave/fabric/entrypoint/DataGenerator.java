package dev.upcraft.sparkweave.fabric.entrypoint;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.datagen.DataGenerationContext;
import dev.upcraft.sparkweave.api.datagen.provider.SparkweaveDynamicRegistryEntryProvider;
import dev.upcraft.sparkweave.api.entrypoint.DataGenerationEntryPoint;
import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.datagen.DynamicRegistryBuilderImpl;
import dev.upcraft.sparkweave.entrypoint.EntrypointHelper;
import dev.upcraft.sparkweave.fabric.impl.datagen.FabricBuiltinEntriesProvider;
import dev.upcraft.sparkweave.fabric.impl.datagen.FabricDataGenerationContext;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.util.Util;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class DataGenerator implements DataGeneratorEntrypoint {

	private static final Set<String> ENABLED_MODS = Util.make(() -> {
		var modIDs = new TreeSet<>(Arrays.asList(System.getProperty("sparkweave.datagen.mods", "").strip().split(",\\s*")));
		if (modIDs.isEmpty()) {
			throw new IllegalArgumentException("[Sparkweave] sparkweave.datagen.mods property was empty or not set! please define which mod(s) to generate data for, as a comma-separated list!");
		}

		// make sure we aren't generating for ourselves
		if (modIDs.size() != 1) {
			modIDs.remove(SparkweaveMod.MODID);
		}

		return Collections.unmodifiableSet(modIDs);
	});

	private final Map<ModContainer, List<SparkweaveDynamicRegistryEntryProvider>> dynamicProviders = Collections.synchronizedMap(new LinkedHashMap<>());
	private RegistrySetBuilder registrySetBuilder;

	private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		this.registrySetBuilder = registryBuilder;

		EntrypointHelper.fireEntrypoints(DataGenerationEntryPoint.class, (dataGenerator, contextModContainer) -> {
			if (ENABLED_MODS.contains(contextModContainer.metadata().id())) {
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

		defaultPack.addProvider((output, registriesFuture1) -> new FabricBuiltinEntriesProvider(output, registriesFuture1, registrySetBuilder, ENABLED_MODS, dynamicProviders));

		EntrypointHelper.fireEntrypoints(DataGenerationEntryPoint.class, (dataGenerator, contextModContainer) -> {
			if (ENABLED_MODS.contains(contextModContainer.metadata().id())) {
				LOGGER.info("Generating data for {}", contextModContainer.metadata().displayName());
				DataGenerationContext ctx = new FabricDataGenerationContext(contextModContainer, strictValidation, defaultPack, registriesFuture, dynamicProviders.get(contextModContainer));
				dataGenerator.generate(ctx);
			}
		});
	}
}
