package dev.upcraft.sparkweave.api.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public record LineRenderable(float startX, float startY, float startZ, float endX, float endY, float endZ, int red, int green, int blue, int alpha, long creationTime, long duration) implements DebugRenderable {

	@Override
	public void render(PoseStack poseStack, VertexConsumer vertexConsumer) {
		var pose = poseStack.last();
		vertexConsumer.addVertex(pose, startX(), startY(), startZ()).setColor(red(), green(), blue(), alpha()).setNormal(pose, 0, 1, 0);
		vertexConsumer.addVertex(pose, endX(), endY(), endZ()).setColor(red(), green(), blue(), alpha()).setNormal(pose, 0, 1, 0);
	}

	@Override
	public long getCreationTimeMs() {
		return creationTime;
	}

	@Override
	public long getRenderDurationMs() {
		return duration;
	}

	@Override
	public RenderType getRenderLayer() {
		return RenderTypes.lines();
	}
}
