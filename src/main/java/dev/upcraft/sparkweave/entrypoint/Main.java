package dev.upcraft.sparkweave.entrypoint;

import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.impl.registry.BlockItemProviderProcessor;
import dev.upcraft.sparkweave.impl.scheduler.ScheduledTaskQueue;
import dev.upcraft.sparkweave.util.SparkweaveLogging;
import net.fabricmc.api.ModInitializer;

@CalledByReflection
public class Main implements ModInitializer {

	@Override
	public void onInitialize() {
		BlockItemProviderProcessor.register();
		ScheduledTaskQueue.init();
		SparkweaveLogging.getLogger().debug("System initialized!");
	}
}
