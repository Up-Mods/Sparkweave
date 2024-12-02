package dev.upcraft.sparkweave.neoforge.event.client;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.client.event.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = SparkweaveMod.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ClientGameBusEvents {

	@SubscribeEvent
	public static void onClientPreTick(ClientTickEvent.Pre event) {
		ClientTickEvents.START_TICK.invoker().startTick(Minecraft.getInstance());
	}

	@SubscribeEvent
	public static void onClientPostTick(ClientTickEvent.Post event) {
		ClientTickEvents.END_TICK.invoker().endTick(Minecraft.getInstance());
	}
}
