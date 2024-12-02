package dev.upcraft.sparkweave.fabric.entrypoint;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.api.client.event.*;
import dev.upcraft.sparkweave.api.client.render.DebugRenderer;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.client.event.RegisterItemPropertiesEventImpl;
import dev.upcraft.sparkweave.entrypoint.EntrypointHelper;
import dev.upcraft.sparkweave.fabric.client.command.ClientRootCommand;
import dev.upcraft.sparkweave.fabric.impl.registry.*;
import dev.upcraft.sparkweave.validation.TranslationChecker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.Collection;
import java.util.Set;

@Environment(EnvType.CLIENT)
@CalledByReflection
public class Client implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		WorldRenderEvents.AFTER_ENTITIES.register((context) -> DebugRenderer.render(context.matrixStack(), context.consumers(), context.camera()));
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) -> {
			if(Minecraft.getInstance().hasSingleplayerServer()) {
				ClientRootCommand.register(dispatcher);
			}
		});

		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			private final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(SparkweaveMod.MODID, "translation_checker");

			@Override
			public ResourceLocation getFabricId() {
				return ID;
			}

			@Override
			public void onResourceManagerReload(ResourceManager resourceManager) {
				TranslationChecker.validate();
			}

			@Override
			public Collection<ResourceLocation> getFabricDependencies() {
				return Set.of(ResourceReloadListenerKeys.LANGUAGES);
			}
		});

		EntrypointHelper.fireEntrypoints(ClientEntryPoint.class, ClientEntryPoint::onInitializeClient);

		RegisterBlockEntityRenderersEvent.EVENT.invoker().registerBlockEntityRenderers(new RegisterBlockEntityRenderersEventImpl());
		RegisterEntityRenderersEvent.EVENT.invoker().registerEntityRenderers(new RegisterEntityRenderersEventImpl());
		RegisterLayerDefinitionsEvent.EVENT.invoker().registerModelLayers(new RegisterLayerDefinitionsEventImpl());
		RegisterCustomArmorRenderersEvent.EVENT.invoker().registerCustomArmorRenderers(new RegisterCustomArmorRenderersEvent());
		RegisterLecternItemRendererEvent.EVENT.invoker().registerBookRenderers(new RegisterLecternItemRendererEvent());
		RegisterItemPropertiesEvent.EVENT.invoker().registerItemProperties(new RegisterItemPropertiesEventImpl());
		RegisterMenuScreensEvent.EVENT.invoker().registerMenuScreens(new RegisterMenuScreensEventImpl());
		RegisterParticleFactoriesEvent.EVENT.invoker().registerParticleFactories(new RegisterParticleFactoriesEventImpl());
		net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.START_CLIENT_TICK.register(client -> ClientTickEvents.START_TICK.invoker().startTick(client));
		net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> ClientTickEvents.END_TICK.invoker().endTick(client));
	}
}
