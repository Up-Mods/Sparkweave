package dev.upcraft.sparkweave.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.upcraft.sparkweave.api.client.render.DebugRenderer;
import dev.upcraft.sparkweave.api.platform.RuntimeEnvironmentType;
import org.joml.Vector3fc;

public class Debug {

	static {
		RuntimeEnvironmentType.CLIENT.orElseThrow();
	}

	public static void setColor(int red, int green, int blue) {
		setColor(red, green, blue, 255);
	}

	public static void setColor(int red, int green, int blue, int alpha) {
		DebugRenderer.setColor(red, green, blue, alpha);
	}

	public static void drawLine(Vector3fc start, Vector3fc end, long duration) {
		drawLine(start.x(), start.y(), start.z(), end.x(), end.y(), end.z(), duration);
	}

	public static void drawLine(float startX, float startY, float startZ, float endX, float endY, float endZ, long duration) {
		DebugRenderer.drawLine(startX, startY, startZ, endX, endY, endZ, duration);
	}

	public static void drawLine(PoseStack poseStack, Vector3fc start, Vector3fc end, long duration) {
		drawLine(poseStack, start.x(), start.y(), start.z(), end.x(), end.y(), end.z(), duration);
	}

	public static void drawLine(PoseStack poseStack, float startX, float startY, float startZ, float endX, float endY, float endZ, long duration) {
		DebugRenderer.drawLine(poseStack, startX, startY, startZ, endX, endY, endZ, duration);
	}
}
