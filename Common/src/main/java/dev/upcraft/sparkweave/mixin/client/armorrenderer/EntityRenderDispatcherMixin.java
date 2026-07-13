package dev.upcraft.sparkweave.mixin.client.armorrenderer;

import com.llamalad7.mixinextras.sugar.Local;
import dev.upcraft.sparkweave.client.event.ArmorRendererRegistry;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

	@Inject(method = "onResourceManagerReload", at = @At("RETURN"))
	private void resetArmorRendererRegistry(ResourceManager resourceManager, CallbackInfo ci, @Local(name = "context") EntityRendererProvider.Context context) {
		ArmorRendererRegistry.onResourceManagerReload(context);
	}
}
