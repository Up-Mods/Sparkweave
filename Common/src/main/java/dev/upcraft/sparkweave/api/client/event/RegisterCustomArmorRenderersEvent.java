package dev.upcraft.sparkweave.api.client.event;

import org.jetbrains.annotations.ApiStatus;

public final class RegisterCustomArmorRenderersEvent {

	@ApiStatus.Internal
	public RegisterCustomArmorRenderersEvent() {
	}

	//FIXME
//	public <ENTITY extends LivingEntity, STATE extends EntityRenderState, MODEL extends EntityModel<? super STATE>> void register(CustomArmorRenderer.Factory<ENTITY, STATE, MODEL> factory, ItemLike... items) {
//		ArmorRendererRegistry.register(factory, items);
//	}
//
//	@SafeVarargs
//	public final <STATE extends EntityRenderState, MODEL extends EntityModel<? super STATE>> void register(CustomArmorRenderer.Factory<Entity, STATE, MODEL> factory, Supplier<? extends ItemLike>... items) {
//		ArmorRendererRegistry.register(factory, items);
//	}

//	public static final Event<RegisterCustomArmorRenderersEvent.Callback> EVENT = Event.create(RegisterCustomArmorRenderersEvent.Callback.class, callbacks -> event -> {
//		ArmorRendererRegistry.prepare();
//		for (RegisterCustomArmorRenderersEvent.Callback callback : callbacks) {
//			callback.registerCustomArmorRenderers(event);
//		}
//	});

	@FunctionalInterface
	public interface Callback {

		void registerCustomArmorRenderers(RegisterCustomArmorRenderersEvent event);
	}
}
