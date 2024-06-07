package dev.upcraft.sparkweave.fabric.impl.registry;

import dev.upcraft.sparkweave.api.client.event.RegisterBlockEntityRenderersEvent;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class RegisterBlockEntityRenderersEventImpl implements RegisterBlockEntityRenderersEvent {

	@Override
	public <T extends BlockEntity> void registerRenderer(Supplier<BlockEntityType<T>> blockEntityType, BlockEntityRendererProvider<T> blockEntityRendererProvider) {
		BlockEntityRenderers.register(blockEntityType.get(), blockEntityRendererProvider);
	}
}
