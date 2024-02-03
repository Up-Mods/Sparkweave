package dev.upcraft.sparkweave.impl.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;

public interface DebugRenderable {

	void render(PoseStack pose, VertexConsumer vertexConsumer);

	long getCreationTimeMs();

	default long getRenderDurationMs() {
		return 1000L; // 1 second
	}

	RenderType getRenderLayer();
}
