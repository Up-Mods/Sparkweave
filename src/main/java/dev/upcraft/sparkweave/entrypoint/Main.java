package dev.upcraft.sparkweave.entrypoint;

import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.api.registry.RegistryService;
import dev.upcraft.sparkweave.command.SparkweaveCommandRoot;
import dev.upcraft.sparkweave.impl.registry.BlockItemProviderProcessor;
import dev.upcraft.sparkweave.impl.scheduler.ScheduledTaskQueue;
import dev.upcraft.sparkweave.registry.SparkweaveCommandArgumentTypes;
import dev.upcraft.sparkweave.util.SparkweaveLogging;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

@CalledByReflection
public class Main implements ModInitializer {

	@Override
	public void onInitialize() {
		BlockItemProviderProcessor.register();
		ScheduledTaskQueue.init();
		CommandRegistrationCallback.EVENT.register(SparkweaveCommandRoot::register);

		var service = RegistryService.get();
		SparkweaveCommandArgumentTypes.ARGUMENT_TYPES.accept(service);

		SparkweaveLogging.getLogger().debug("System initialized!");
	}
}
