package dev.upcraft.sparkweave.api.client.event;

import dev.upcraft.sparkweave.api.client.render.LecternItemRenderer;
import dev.upcraft.sparkweave.api.event.Event;
import dev.upcraft.sparkweave.client.event.LecternItemRendererRegistryImpl;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class RegisterLecternItemRendererEvent {
	public void registerRenderer(LecternItemRenderer.Factory factory, ItemLike item) {
		registerRenderer(factory, () -> item);
	}

	public void registerRenderer(LecternItemRenderer.Factory factory, Supplier<? extends ItemLike> item) {
		LecternItemRendererRegistryImpl.register(factory, item);
	}

	public static final Event<RegisterLecternItemRendererEvent.Callback> EVENT = Event.create(RegisterLecternItemRendererEvent.Callback.class, callbacks -> event -> {
		for (RegisterLecternItemRendererEvent.Callback callback : callbacks) {
			callback.registerBookRenderers(event);
		}
	});

	@FunctionalInterface
	public interface Callback {
		void registerBookRenderers(RegisterLecternItemRendererEvent event);
	}
}
