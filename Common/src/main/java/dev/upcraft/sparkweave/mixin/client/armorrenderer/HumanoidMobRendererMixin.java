package dev.upcraft.sparkweave.mixin.client.armorrenderer;

import dev.upcraft.sparkweave.client.render.RenderHooks;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("deprecation")
@Mixin(HumanoidMobRenderer.class)
public abstract class HumanoidMobRendererMixin<T extends Mob, S extends HumanoidRenderState, M extends HumanoidModel<S>> extends AgeableMobRenderer<T, S, M> {

	private HumanoidMobRendererMixin(EntityRendererProvider.Context context, M adultModel, M babyModel, float shadow) {
		super(context, adultModel, babyModel, shadow);
		throw new UnsupportedOperationException();
	}

	@Inject(method = "extractHumanoidRenderState", at = @At("RETURN"))
	private static void extractCustomRenderState(LivingEntity entity, HumanoidRenderState state, float partialTicks, ItemModelResolver itemModelResolver, CallbackInfo ci) {
		RenderHooks.extractHumanoidRenderState(entity, state, partialTicks, itemModelResolver);
	}
}
