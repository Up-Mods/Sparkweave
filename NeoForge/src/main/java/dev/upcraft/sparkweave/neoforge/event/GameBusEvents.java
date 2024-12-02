package dev.upcraft.sparkweave.neoforge.event;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.event.CommandEvents;
import dev.upcraft.sparkweave.event.EntityTickEventsImpl;
import dev.upcraft.sparkweave.scheduler.ScheduledTaskQueue;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = SparkweaveMod.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GameBusEvents {

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
