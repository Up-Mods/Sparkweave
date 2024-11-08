package dev.upcraft.sparkweave.fabric.entrypoint;

import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.event.CommandEvents;
import dev.upcraft.sparkweave.api.event.CustomLecternMenuEvent;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.api.registry.block.BlockItemProvider;
import dev.upcraft.sparkweave.entrypoint.EntrypointHelper;
import dev.upcraft.sparkweave.logging.SparkweaveLogging;
import dev.upcraft.sparkweave.registry.SparkweaveCommandArgumentTypes;
import dev.upcraft.sparkweave.scheduler.ScheduledTaskQueue;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

@CalledByReflection
public class Main implements ModInitializer {

	@Override
	public void onInitialize() {
		RegistryService.get().visitRegistry(BuiltInRegistries.BLOCK, (id, block) -> {
			if (block instanceof BlockItemProvider provider) {
				Registry.register(BuiltInRegistries.ITEM, id, provider.createItem());
			}
		});

		ServerLifecycleEvents.SERVER_STARTING.register(ScheduledTaskQueue::onServerStarting);
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> ScheduledTaskQueue.onServerStopped());
		ServerTickEvents.START_SERVER_TICK.register(server -> ScheduledTaskQueue.onServerTick());

		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> CommandEvents.REGISTER.invoker().registerCommands(dispatcher, buildContext, environment));
		CustomLecternMenuEvent.EVENT.invoker().registerLecternMenus(new CustomLecternMenuEvent());

		var service = RegistryService.get();
		SparkweaveCommandArgumentTypes.ARGUMENT_TYPES.accept(service);

		EntrypointHelper.fireEntrypoints(MainEntryPoint.class, MainEntryPoint::onInitialize);

		SparkweaveLogging.getLogger().debug("System initialized!");
	}
}
