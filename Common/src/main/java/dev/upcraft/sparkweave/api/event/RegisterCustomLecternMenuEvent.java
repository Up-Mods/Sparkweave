package dev.upcraft.sparkweave.api.event;

import dev.upcraft.sparkweave.event.LecternMenuRegistry;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class RegisterCustomLecternMenuEvent {
	public void register(LecternMenuRegistry.MenuProviderFactory factory, ItemLike item) {
		LecternMenuRegistry.register(factory, item);
	}

	public void register(LecternMenuRegistry.MenuProviderFactory factory, Supplier<ItemLike> item) {
		LecternMenuRegistry.register(factory, item);
	}

	public static final Event<RegisterCustomLecternMenuEvent.Callback> EVENT = Event.create(RegisterCustomLecternMenuEvent.Callback.class, listeners -> event -> {
		for(RegisterCustomLecternMenuEvent.Callback listener : listeners)
			listener.registerLecternMenus(event);
	});

	@FunctionalInterface
	public interface Callback {
		void registerLecternMenus(RegisterCustomLecternMenuEvent event);
	}
}
