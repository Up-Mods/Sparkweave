package dev.upcraft.sparkweave.mixin.client.ext;

import dev.upcraft.sparkweave.api.client.ext.RenderLayerExt;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderLayer.class)
public abstract class RenderLayerMixin<S extends EntityRenderState, M extends EntityModel<? super S>> implements RenderLayerExt<S, M> {

	@Accessor("renderer")
	@Override
	public abstract RenderLayerParent<S, M> sparkweave$getParentRenderer();
}
