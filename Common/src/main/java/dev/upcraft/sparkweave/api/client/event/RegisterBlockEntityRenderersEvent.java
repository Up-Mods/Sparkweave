package dev.upcraft.sparkweave.api.client.event;

import dev.upcraft.sparkweave.api.event.Event;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface RegisterBlockEntityRenderersEvent {

	<T extends BlockEntity, S extends BlockEntityRenderState> void registerRenderer(Supplier<BlockEntityType<T>> blockEntityType, BlockEntityRendererProvider<T, S> blockEntityRendererProvider);

	Event<Callback> EVENT = Event.create(Callback.class, callbacks -> event -> {
		for (Callback callback : callbacks) {
			callback.registerBlockEntityRenderers(event);
		}
	});

	@FunctionalInterface
	interface Callback {
		void registerBlockEntityRenderers(RegisterBlockEntityRenderersEvent event);
	}
}
