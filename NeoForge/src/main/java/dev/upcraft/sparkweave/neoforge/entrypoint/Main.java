package dev.upcraft.sparkweave.neoforge.entrypoint;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.entrypoint.DedicatedServerEntryPoint;
import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.event.CommandEvents;
import dev.upcraft.sparkweave.api.event.RegisterCustomLecternMenuEvent;
import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.api.registry.block.BlockItemProvider;
import dev.upcraft.sparkweave.api.registry.block.InjectIntoBlockEntity;
import dev.upcraft.sparkweave.entrypoint.EntrypointHelper;
import dev.upcraft.sparkweave.event.EntityTickEventsImpl;
import dev.upcraft.sparkweave.registry.SparkweaveCommandArgumentTypes;
import dev.upcraft.sparkweave.scheduler.ScheduledTaskQueue;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@CalledByReflection
@EventBusSubscriber(modid = SparkweaveMod.MODID)
@Mod(SparkweaveMod.MODID)
public class Main {

	public Main(IEventBus bus) {
		var helper = RegistryService.get();
		SparkweaveCommandArgumentTypes.ARGUMENT_TYPES.accept(helper);

		EntrypointHelper.fireEntrypoints(MainEntryPoint.class, MainEntryPoint::onInitialize);

		switch (FMLEnvironment.getDist()) {
			case CLIENT ->
				EntrypointHelper.fireEntrypoints(ClientEntryPoint.class, ClientEntryPoint::onInitializeClient);
			case DEDICATED_SERVER ->
				EntrypointHelper.fireEntrypoints(DedicatedServerEntryPoint.class, DedicatedServerEntryPoint::onInitializeServer);
		}

		SparkweaveLoggerFactory.getLogger().debug("System initialized!");
	}

	@SubscribeEvent
	public static void processBlockEntityInjections(BlockEntityTypeAddBlocksEvent event) {
		BuiltInRegistries.BLOCK.forEach(block -> {
			if(block instanceof InjectIntoBlockEntity inject) {
				for (BlockEntityType<?> blockEntityType : inject.getBlockEntityTypesToInjectInto()) {
					event.modify(blockEntityType, block);
				}
			}
		});
	}

	@SubscribeEvent
	public static void processBlockItems(RegisterEvent event) {
		event.register(Registries.ITEM, registerHelper -> BuiltInRegistries.BLOCK.entrySet().forEach(entry -> {
			if (entry.getValue() instanceof BlockItemProvider provider) {
				var itemId = provider.createItemId(entry.getKey());
				var properties = new Item.Properties().useBlockDescriptionPrefix().setId(itemId);
				registerHelper.register(itemId, provider.createItem(properties));
			}
		}));
	}

	@SubscribeEvent
	public static void onCommonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			RegisterCustomLecternMenuEvent.EVENT.invoker().registerLecternMenus(new RegisterCustomLecternMenuEvent());
		});
	}

	@SubscribeEvent
	public static void onServerStart(ServerStartingEvent event) {
		ScheduledTaskQueue.onServerStarting(event.getServer());
	}

	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event) {
		ScheduledTaskQueue.onServerStopped();
	}

	@SubscribeEvent
	public static void onServerTick(ServerTickEvent.Pre event) {
		ScheduledTaskQueue.onServerTick();
	}

	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event) {
		CommandEvents.REGISTER.invoker().registerCommands(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
	}

	@SubscribeEvent
	public static void preEntityTick(EntityTickEvent.Pre event) {
		var entity = event.getEntity();
		if(EntityTickEventsImpl.getStartHandler(entity.getClass()).invoker().startTick(entity, entity.level())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void postEntityTick(EntityTickEvent.Post event) {
		var entity = event.getEntity();
		EntityTickEventsImpl.getEndHandler(entity.getClass()).invoker().endTick(entity, entity.level());
	}
}
