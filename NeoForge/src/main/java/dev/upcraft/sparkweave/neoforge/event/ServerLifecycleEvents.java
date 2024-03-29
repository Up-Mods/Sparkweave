package dev.upcraft.sparkweave.neoforge.event;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.scheduler.ScheduledTaskQueue;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

@Mod.EventBusSubscriber(modid = SparkweaveMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerLifecycleEvents {

	@SubscribeEvent
	public static void onServerStart(ServerStartingEvent event) {
		ScheduledTaskQueue.onServerStarting(event.getServer());
	}

	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event) {
		ScheduledTaskQueue.onServerStopped();
	}

	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event) {
		if(event.phase == TickEvent.Phase.START) {
			ScheduledTaskQueue.onServerTick();
		}
	}
}
