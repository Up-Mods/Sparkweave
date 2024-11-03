package dev.upcraft.sparkweave.neoforge.mixin.client;

import dev.upcraft.sparkweave.api.client.event.RegisterBlockEntityRenderersEvent;
import dev.upcraft.sparkweave.api.client.event.RegisterEntityRenderersEvent;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Implements({
	@Interface(iface = RegisterEntityRenderersEvent.class, prefix = "registerEntityRenderers$"),
	@Interface(iface = RegisterBlockEntityRenderersEvent.class, prefix = "registerBlockEntityRenderer$")
})
@Mixin(EntityRenderersEvent.RegisterRenderers.class)
public abstract class RegisterRenderersEventMixin implements RegisterEntityRenderersEvent, RegisterBlockEntityRenderersEvent {

	@Shadow
	public abstract <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> blockEntityRendererProvider);

	@Shadow
	public abstract <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider);

	public <T extends BlockEntity> void registerBlockEntityRenderer$registerRenderer(Supplier<BlockEntityType<T>> blockEntityType, BlockEntityRendererProvider<T> blockEntityRendererProvider) {
		this.registerBlockEntityRenderer(blockEntityType.get(), blockEntityRendererProvider);
	}

	public <T extends Entity> void registerEntityRenderers$registerRenderer(Supplier<EntityType<T>> entityType, EntityRendererProvider<T> entityRendererProvider) {
		this.registerEntityRenderer(entityType.get(), entityRendererProvider);
	}
}
