package dev.upcraft.sparkweave.client.debug;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.upcraft.sparkweave.api.client.render.DebugRenderable;
import dev.upcraft.sparkweave.api.client.render.LineRenderable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class SparkweaveDebugRenderer {

	private static final Multimap<RenderType, DebugRenderable> renderables = MultimapBuilder.hashKeys().linkedListValues().build();
	private static final List<DebugRenderable> expired = new ArrayList<>(10);
	private static final int[] colors = {255, 255, 255, 255};

	public static void setColor(int red, int green, int blue, int alpha) {
		colors[0] = red;
		colors[1] = green;
		colors[2] = blue;
		colors[3] = alpha;
	}

	public static void add(DebugRenderable renderable) {
		renderables.put(renderable.getRenderLayer(), renderable);
	}

	public static void drawLine(float startX, float startY, float startZ, float endX, float endY, float endZ, long duration) {
		add(new LineRenderable(startX, startY, startZ, endX, endY, endZ, colors[0], colors[1], colors[2], colors[3], Util.getMillis(), duration));
	}

	public static void drawLine(PoseStack matrices, float startX, float startY, float startZ, float endX, float endY, float endZ, long duration) {
		Matrix4f model = matrices.last().pose();
		Vector3f start = model.transformPosition(new Vector3f(startX, startY, startZ));
		Vector3f end = model.transformPosition(new Vector3f(endX, endY, endZ));
		drawLine(start.x(), start.y(), start.z(), end.x(), end.y(), end.z(), duration);
	}

	public static void render(PoseStack matrices, MultiBufferSource bufferSource, LevelRenderState levelRenderState) {
		if(!renderables.isEmpty()) {
			setColor(255, 255, 255, 255);
			long currentTime = Util.getMillis();
			Vec3 cameraPos = levelRenderState.cameraRenderState.pos;
			matrices.pushPose();
			matrices.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());
			for (RenderType renderLayer : renderables.keySet()) {
				VertexConsumer buffer = bufferSource.getBuffer(renderLayer);

				renderables.get(renderLayer).forEach(it -> {
					it.render(matrices, buffer);
					if (it.getCreationTimeMs() + it.getRenderDurationMs() <= currentTime) {
						expired.add(it);
					}
				});
			}
			matrices.popPose();
			if (!expired.isEmpty()) {
				expired.forEach(it -> renderables.remove(it.getRenderLayer(), it));
				expired.clear();
			}
		}
	}
}
