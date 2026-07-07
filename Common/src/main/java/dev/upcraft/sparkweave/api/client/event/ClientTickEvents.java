package dev.upcraft.sparkweave.api.client.event;

import dev.upcraft.sparkweave.api.event.Event;
import dev.upcraft.sparkweave.event.EventFactoryImpl;
import net.minecraft.client.Minecraft;

public class ClientTickEvents {

	public static final Event<StartTick> START_TICK = EventFactoryImpl.create(StartTick.class, (listeners) -> client -> {
		for (StartTick listener : listeners) {
			listener.startTick(client);
		}
	});

	public static final Event<EndTick> END_TICK = EventFactoryImpl.create(EndTick.class, (listeners) -> client -> {
		for (EndTick handler : listeners) {
			handler.endTick(client);
		}
	});

	@FunctionalInterface
	public interface StartTick {
		void startTick(Minecraft client);
	}

	@FunctionalInterface
	public interface EndTick {
		void endTick(Minecraft client);
	}
}
