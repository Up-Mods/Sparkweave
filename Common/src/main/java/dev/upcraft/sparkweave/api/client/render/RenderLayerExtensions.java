package dev.upcraft.sparkweave.api.client.render;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

/**
 * implemented by every {@link RenderLayer}
 */
public interface RenderLayerExtensions<T extends LivingEntity,M extends HumanoidModel<T>> {

	RenderLayerParent<T, M> sparkweave$getParent();
}
