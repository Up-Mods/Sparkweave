package dev.upcraft.sparkweave.mixin.client;

import dev.upcraft.sparkweave.api.client.render.RenderLayerExtensions;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderLayer.class)
public abstract class RenderLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>> implements RenderLayerExtensions<T, M> {

	@Accessor("renderer")
	@Override
	public abstract RenderLayerParent<T, M> sparkweave$getParent();
}
