package dev.upcraft.sparkweave.mixin.client.armorrenderer;

import dev.upcraft.sparkweave.client.render.RenderHooks;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity>
	extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {

	private AvatarRendererMixin(EntityRendererProvider.Context context, PlayerModel model, float shadow) {
		super(context, model, shadow);
		throw new UnsupportedOperationException();
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("RETURN"))
	private void extractCustomRenderState(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
		RenderHooks.extractHumanoidRenderState(this, entity, state, this.itemModelResolver, partialTicks);
	}
}
