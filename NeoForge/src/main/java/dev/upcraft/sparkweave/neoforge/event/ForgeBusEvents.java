package dev.upcraft.sparkweave.neoforge.event;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.event.CommandEvents;
import dev.upcraft.sparkweave.scheduler.ScheduledTaskQueue;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = SparkweaveMod.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ForgeBusEvents {

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
}
