package dev.upcraft.sparkweave.fabric.impl.registry;

import dev.upcraft.sparkweave.api.client.event.RegisterEntityRenderersEvent;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public class RegisterEntityRenderersEventImpl implements RegisterEntityRenderersEvent {

	@Override
	public <T extends Entity> void registerRenderer(Supplier<EntityType<T>> entityType, EntityRendererProvider<T> entityRendererProvider) {
		EntityRenderers.register(entityType.get(), entityRendererProvider);
	}
}
