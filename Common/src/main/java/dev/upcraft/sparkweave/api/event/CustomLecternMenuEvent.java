package dev.upcraft.sparkweave.api.event;

import dev.upcraft.sparkweave.event.LecternMenuRegistry;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class CustomLecternMenuEvent {
	public void register(LecternMenuRegistry.MenuFactory factory, ItemLike item) {
		LecternMenuRegistry.register(factory, item);
	}

	public void register(LecternMenuRegistry.MenuFactory factory, Supplier<ItemLike> item) {
		LecternMenuRegistry.register(factory, item);
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
