package dev.upcraft.sparkweave.api.client.event;

import dev.upcraft.sparkweave.api.event.Event;
import dev.upcraft.sparkweave.event.EventFactoryImpl;
import net.minecraft.client.Minecraft;

public interface ClientTickEvents {
	Event<StartTick> START_TICK = EventFactoryImpl.create(StartTick.class, (listeners) -> client -> {
		for(StartTick listener : listeners) {
			if(listener.startOfTick(client))
				return true;
		}

		return false;
	});

	Event<EndTick> END_TICK = EventFactoryImpl.create(EndTick.class, (listeners) -> client -> {
		for(EndTick handler : listeners)
			handler.endOfTick(client);
	});

	@FunctionalInterface
	interface StartTick {
		boolean startOfTick(Minecraft client);
	}

	@FunctionalInterface
	interface EndTick {
		void endOfTick(Minecraft client);
	}
}
