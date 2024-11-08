package dev.upcraft.sparkweave.api.event;

import dev.upcraft.sparkweave.event.LecternMenuRegistry;
import net.minecraft.world.level.ItemLike;

public class CustomLecternMenuEvent {
	public void register(ItemLike item, LecternMenuRegistry.MenuFactory factory) {
		System.out.println("THIS IS A TEST");
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
