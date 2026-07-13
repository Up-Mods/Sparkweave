package dev.upcraft.sparkweave.api.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface DebugRenderable {

	void render(PoseStack pose, VertexConsumer vertexConsumer);

	long getCreationTimeMs();

	default long getRenderDurationMs() {
		return 1000L; // 1 second
	}

	RenderType getRenderLayer();
}
