package dev.upcraft.sparkweave.entrypoint;

import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.impl.client.render.DebugRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

@Environment(EnvType.CLIENT)
@CalledByReflection
public class Client implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		WorldRenderEvents.AFTER_ENTITIES.register((context) -> DebugRenderer.render(context.matrixStack(), context.consumers(), context.camera()));
	}
}
