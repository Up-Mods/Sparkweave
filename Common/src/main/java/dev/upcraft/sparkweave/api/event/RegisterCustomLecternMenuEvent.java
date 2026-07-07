package dev.upcraft.sparkweave.api.event;

import dev.upcraft.sparkweave.event.LecternMenuRegistry;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class RegisterCustomLecternMenuEvent {
	public void register(Supplier<? extends ItemLike> item, LecternMenuRegistry.MenuProviderFactory factory) {
		LecternMenuRegistry.register(item, factory);
	}

	public void register(ItemLike item, LecternMenuRegistry.MenuProviderFactory factory) {
		register(() -> item, factory);
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
