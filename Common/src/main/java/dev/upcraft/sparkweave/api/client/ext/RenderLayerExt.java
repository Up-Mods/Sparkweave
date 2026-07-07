package dev.upcraft.sparkweave.api.client.ext;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

/**
 * implemented by every {@link RenderLayer}
 */
public interface RenderLayerExt<S extends EntityRenderState, M extends EntityModel<? super S>> {

	default RenderLayerParent<S, M> sparkweave$getParentRenderer() {
		throw new AssertionError("Implemented in Mixin");
	}
}
