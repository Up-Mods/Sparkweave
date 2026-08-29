package dev.upcraft.sparkweave.fabric.entrypoint;

import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.event.CommandEvents;
import dev.upcraft.sparkweave.api.event.RegisterCustomLecternMenuEvent;
import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.api.registry.block.BlockItemProvider;
import dev.upcraft.sparkweave.api.registry.block.InjectIntoBlockEntity;
import dev.upcraft.sparkweave.entrypoint.EntrypointHelper;
import dev.upcraft.sparkweave.registry.SparkweaveCommandArgumentTypes;
import dev.upcraft.sparkweave.scheduler.ScheduledTaskQueue;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;

@CalledByReflection
public class Main implements ModInitializer {

	@Override
	public void onInitialize() {
		RegistryService.get().visitRegistry(BuiltInRegistries.BLOCK, (id, block) -> {
			if (block instanceof BlockItemProvider provider) {
				var itemId = provider.createItemId(ResourceKey.create(Registries.BLOCK, id));
				var properties = new Item.Properties().useBlockDescriptionPrefix().setId(itemId);
				Registry.register(BuiltInRegistries.ITEM, itemId, provider.createItem(properties));
			}

			if(block instanceof InjectIntoBlockEntity inject) {
				for (BlockEntityType<?> blockEntityType : inject.getBlockEntityTypesToInjectInto()) {
					blockEntityType.addValidBlock(block);
				}
			}
		});

		ServerLifecycleEvents.SERVER_STARTING.register(ScheduledTaskQueue::onServerStarting);
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> ScheduledTaskQueue.onServerStopped());
		ServerTickEvents.START_SERVER_TICK.register(server -> ScheduledTaskQueue.onServerTick());

		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> CommandEvents.REGISTER.invoker().registerCommands(dispatcher, buildContext, environment));

		var service = RegistryService.get();
		SparkweaveCommandArgumentTypes.ARGUMENT_TYPES.accept(service);

		EntrypointHelper.fireEntrypoints(MainEntryPoint.class, MainEntryPoint::onInitialize);

		RegisterCustomLecternMenuEvent.EVENT.invoker().registerLecternMenus(new RegisterCustomLecternMenuEvent());

		SparkweaveLoggerFactory.getLogger().debug("System initialized!");
	}
}
