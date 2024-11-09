package dev.upcraft.sparkweave.api.event;

import dev.upcraft.sparkweave.event.LecternMenuRegistry;
import net.minecraft.world.level.ItemLike;

public class CustomLecternMenuEvent {
	public void register(LecternMenuRegistry.MenuFactory factory, ItemLike item) {
		LecternMenuRegistry.register(item, factory);
	}

	public static final Event<CustomLecternMenuEvent.Callback> EVENT = Event.create(CustomLecternMenuEvent.Callback.class, listeners -> event -> {
		for(CustomLecternMenuEvent.Callback listener : listeners)
			listener.registerLecternMenus(event);
	});

	@FunctionalInterface
	public interface Callback {
		void registerLecternMenus(CustomLecternMenuEvent event);
	}
}
