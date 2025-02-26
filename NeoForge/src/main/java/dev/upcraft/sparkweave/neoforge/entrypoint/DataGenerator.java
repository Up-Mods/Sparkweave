package dev.upcraft.sparkweave.neoforge.entrypoint;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.datagen.DataGenerationContext;
import dev.upcraft.sparkweave.datagen.DynamicRegistryBuilderImpl;
import dev.upcraft.sparkweave.api.datagen.provider.SparkweaveDynamicRegistryEntryProvider;
import dev.upcraft.sparkweave.api.entrypoint.DataGenerationEntryPoint;
import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.entrypoint.EntrypointHelper;
import dev.upcraft.sparkweave.neoforge.impl.datagen.NeoBuiltinEntriesProvider;
import dev.upcraft.sparkweave.neoforge.impl.datagen.NeoDataGenerationContext;
import dev.upcraft.sparkweave.neoforge.mixin.datagen.GatherDataEventAccessor;
import net.minecraft.Util;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.logging.log4j.Logger;

import java.util.*;

@EventBusSubscriber(modid = SparkweaveMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerator {

	private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();

	@SubscribeEvent
	public static void onDataGeneration(GatherDataEvent event) {
		var rootGenerator = event.getGenerator();

		Set<String> mods = Util.make(() -> {
			var modIDs = new TreeSet<>(((GatherDataEventAccessor) event).sparkweave$getConfig().getMods());
			if (modIDs.isEmpty()) {
				throw new IllegalArgumentException("[Sparkweave] No --mod parameter provided! please define which mod(s) to generate data for!");
			}

			// make sure we aren't generating for ourselves
			if (modIDs.size() != 1) {
				modIDs.remove(SparkweaveMod.MODID);
			}

			return Collections.unmodifiableSet(modIDs);
		});

		var registrySetBuilder = new RegistrySetBuilder();
		Map<ModContainer, List<SparkweaveDynamicRegistryEntryProvider>> dynamicProviders = Collections.synchronizedMap(new LinkedHashMap<>());

		// first gather ALL the dynamic entries
		EntrypointHelper.fireEntrypoints(DataGenerationEntryPoint.class, (dataGenerator, contextModContainer) -> {
			if (mods.contains(contextModContainer.metadata().id())) {
				LOGGER.info("Gathering dynamic registry data for {}", contextModContainer.metadata().displayName());
				var dynamicRegistryBuilder = new DynamicRegistryBuilderImpl(contextModContainer, registrySetBuilder);
				dataGenerator.generateDynamicRegistryEntries(dynamicRegistryBuilder);
				dynamicProviders.put(contextModContainer, dynamicRegistryBuilder.getProviders());
			}
		});

		var registriesFuture = rootGenerator.addProvider(event.includeServer(), (DataProvider.Factory<NeoBuiltinEntriesProvider>) (PackOutput output) -> new NeoBuiltinEntriesProvider(output, event.getLookupProvider(), registrySetBuilder, mods, dynamicProviders)).getRegistryProvider();

		// need to wait here so all the data is ready before we proceed!
		registriesFuture.join();

		// then register everything else
		EntrypointHelper.fireEntrypoints(DataGenerationEntryPoint.class, (dataGenerator, contextModContainer) -> {
			if (mods.contains(contextModContainer.metadata().id())) {
				LOGGER.info("Generating data for {}", contextModContainer.metadata().displayName());
				DataGenerationContext ctx = new NeoDataGenerationContext(contextModContainer, rootGenerator, registriesFuture, event, dynamicProviders.get(contextModContainer));
				dataGenerator.generate(ctx);
			}
		});
	}
}
