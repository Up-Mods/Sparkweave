package dev.upcraft.sparkweave.mixin.client.lectern;

import dev.upcraft.sparkweave.client.event.LecternItemRendererRegistryImpl;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(BlockEntityRenderers.class)
public class BlockEntityRenderersMixin {

	@Inject(method = "createEntityRenderers", at = @At("RETURN"))
	private static void onCreateRenderers(BlockEntityRendererProvider.Context context, CallbackInfoReturnable<Map<BlockEntityType<?>, BlockEntityRenderer<?, ?>>> cir) {
		LecternItemRendererRegistryImpl.onResourceManagerReload(context);
	}
}
