package dev.upcraft.sparkweave.api.client.event;

import dev.upcraft.sparkweave.api.client.render.CustomArmorRenderer;
import dev.upcraft.sparkweave.api.event.Event;
import dev.upcraft.sparkweave.client.event.ArmorRendererRegistry;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

public final class RegisterCustomArmorRenderersEvent {

	@ApiStatus.Internal
	public RegisterCustomArmorRenderersEvent() {
	}

	public <E extends LivingEntity, M extends EntityModel<E>> void register(CustomArmorRenderer.Factory<E, M> factory, ItemLike... items) {
		ArmorRendererRegistry.register(factory, items);
	}

	@SafeVarargs
	public final <E extends LivingEntity, M extends EntityModel<E>> void register(CustomArmorRenderer.Factory<E, M> factory, Supplier<? extends ItemLike>... items) {
		ArmorRendererRegistry.register(factory, items);
	}

	public static final Event<RegisterCustomArmorRenderersEvent.Callback> EVENT = Event.create(RegisterCustomArmorRenderersEvent.Callback.class, callbacks -> event -> {
		ArmorRendererRegistry.prepare();
		for (RegisterCustomArmorRenderersEvent.Callback callback : callbacks) {
			callback.registerCustomArmorRenderers(event);
		}
	});

	@FunctionalInterface
	public interface Callback {

		void registerCustomArmorRenderers(RegisterCustomArmorRenderersEvent event);
	}
}
