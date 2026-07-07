package dev.upcraft.sparkweave.neoforge.mixin.client.renderstateext;

import dev.upcraft.sparkweave.api.client.ext.RenderStateExt;
import net.neoforged.neoforge.client.renderstate.BaseRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BaseRenderState.class)
public abstract class BaseRenderStateMixin implements RenderStateExt {
}
