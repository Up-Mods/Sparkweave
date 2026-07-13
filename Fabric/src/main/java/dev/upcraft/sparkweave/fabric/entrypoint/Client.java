package dev.upcraft.sparkweave.fabric.entrypoint;

import com.mojang.brigadier.CommandDispatcher;
import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.SparkweaveApi;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.api.client.command.ClientCommandSource;
import dev.upcraft.sparkweave.api.client.event.*;
import dev.upcraft.sparkweave.client.debug.SparkweaveDebugRenderer;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.entrypoint.EntrypointHelper;
import dev.upcraft.sparkweave.fabric.impl.registry.*;
import dev.upcraft.sparkweave.validation.TranslationChecker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

@Environment(EnvType.CLIENT)
@CalledByReflection
public class Client implements ClientModInitializer {
	@SuppressWarnings("unchecked")
	@Override
	public void onInitializeClient() {
		LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register(context -> SparkweaveDebugRenderer.render(context.poseStack(), context.bufferSource(), context.levelState()));

		if(SparkweaveApi.Client.LOG_MISSING_TRANSLATIONS) {
			var id = SparkweaveMod.id("translation_checker");
			var loader = ResourceLoader.get(PackType.CLIENT_RESOURCES);
			loader.registerReloadListener(id, (ResourceManagerReloadListener) _ -> TranslationChecker.validate());
			loader.addListenerOrdering(ResourceReloaderKeys.Client.LANGUAGES, id);
		}

		EntrypointHelper.fireEntrypoints(ClientEntryPoint.class, ClientEntryPoint::onInitializeClient);

		RegisterBlockEntityRenderersEvent.EVENT.invoker().registerBlockEntityRenderers(new RegisterBlockEntityRenderersEventImpl());
		RegisterEntityRenderersEvent.EVENT.invoker().registerEntityRenderers(new RegisterEntityRenderersEventImpl());
		RegisterLayerDefinitionsEvent.EVENT.invoker().registerModelLayers(new RegisterLayerDefinitionsEventImpl());
		RegisterCustomArmorRenderersEvent.EVENT.invoker().registerCustomArmorRenderers(new RegisterCustomArmorRenderersEvent());
		RegisterLecternItemRendererEvent.EVENT.invoker().registerBookRenderers(new RegisterLecternItemRendererEvent());
		RegisterMenuScreensEvent.EVENT.invoker().registerMenuScreens(new RegisterMenuScreensEventImpl());
		RegisterParticleProvidersEvent.EVENT.invoker().registerParticleFactories(new RegisterParticleProvidersEventImpl());
		net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.START_CLIENT_TICK.register(client -> ClientTickEvents.START_TICK.invoker().startTick(client));
		net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> ClientTickEvents.END_TICK.invoker().endTick(client));
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) -> ClientCommandEvents.REGISTER.invoker().registerClientCommands((CommandDispatcher<ClientCommandSource>)(Object) dispatcher, buildContext));
	}
}
