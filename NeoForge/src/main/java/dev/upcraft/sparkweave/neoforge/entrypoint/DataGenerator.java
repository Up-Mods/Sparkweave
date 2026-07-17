package dev.upcraft.sparkweave.neoforge.entrypoint;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.datagen.DataGenerationContext;
import dev.upcraft.sparkweave.api.datagen.provider.common.dynamic.SparkweaveDynamicRegistryEntryProvider;
import dev.upcraft.sparkweave.api.entrypoint.DataGenerationEntryPoint;
import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.datagen.DynamicRegistryBuilderImpl;
import dev.upcraft.sparkweave.datagen.SparkweaveDatagenHelper;
import dev.upcraft.sparkweave.entrypoint.EntrypointHelper;
import dev.upcraft.sparkweave.neoforge.impl.datagen.NeoBuiltinEntriesProvider;
import dev.upcraft.sparkweave.neoforge.impl.datagen.NeoDataGenerationContext;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = SparkweaveMod.MODID)
public class DataGenerator {

	private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();

	@SubscribeEvent
	public static void onDataGenerationClient(GatherDataEvent.Client event) {
		runDataGeneration(event, true);
	}

	@SubscribeEvent
	public static void onDataGenerationServer(GatherDataEvent.Server event) {
		runDataGeneration(event, false);
	}

	private static void runDataGeneration(GatherDataEvent event, boolean includeClient) {
		var rootGenerator = event.getGenerator();

		var registrySetBuilder = new RegistrySetBuilder();
		Map<ModContainer, List<SparkweaveDynamicRegistryEntryProvider>> dynamicProviders = Collections.synchronizedMap(new LinkedHashMap<>());

		// first gather ALL the dynamic entries
		EntrypointHelper.fireEntrypoints(DataGenerationEntryPoint.class, (dataGenerator, contextModContainer) -> {
			if (SparkweaveDatagenHelper.ENABLED_MODS.contains(contextModContainer.metadata().id())) {
				LOGGER.info("Gathering dynamic registry data for {}", contextModContainer.metadata().displayName());
				var dynamicRegistryBuilder = new DynamicRegistryBuilderImpl(contextModContainer, registrySetBuilder);
				dataGenerator.generateDynamicRegistryEntries(dynamicRegistryBuilder);
				dynamicProviders.put(contextModContainer, dynamicRegistryBuilder.getProviders());
			}
		});

		var registriesFuture = rootGenerator.addProvider(true, (DataProvider.Factory<NeoBuiltinEntriesProvider>) (PackOutput output) -> new NeoBuiltinEntriesProvider(output, event.getLookupProvider(), registrySetBuilder, SparkweaveDatagenHelper.ENABLED_MODS, dynamicProviders)).getRegistryProvider();

		// need to wait here so all the data is ready before we proceed!
		registriesFuture.join();

		// then register everything else
		EntrypointHelper.fireEntrypoints(DataGenerationEntryPoint.class, (dataGenerator, contextModContainer) -> {
			if (SparkweaveDatagenHelper.ENABLED_MODS.contains(contextModContainer.metadata().id())) {
				LOGGER.info("Generating data for {}", contextModContainer.metadata().displayName());
				DataGenerationContext ctx = new NeoDataGenerationContext(contextModContainer, rootGenerator, registriesFuture, event, includeClient, dynamicProviders.get(contextModContainer));
				dataGenerator.generate(ctx);
			}
		});
	}
}
