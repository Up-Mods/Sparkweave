package dev.upcraft.sparkweave.neoforge.entrypoint;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.SparkweaveApi;
import dev.upcraft.sparkweave.api.client.event.*;
import dev.upcraft.sparkweave.api.client.event.RegisterMenuScreensEvent;
import dev.upcraft.sparkweave.api.client.render.DebugRenderer;
import dev.upcraft.sparkweave.client.event.RegisterItemPropertiesEventImpl;
import dev.upcraft.sparkweave.neoforge.impl.registry.RegisterParticleFactoriesEventImpl;
import dev.upcraft.sparkweave.validation.TranslationChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;

@EventBusSubscriber(modid = SparkweaveMod.MODID, value = Dist.CLIENT)
public class Client {

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			RegisterItemPropertiesEvent.EVENT.invoker().registerItemProperties(new RegisterItemPropertiesEventImpl());
		});
	}

	@SubscribeEvent
	public static void onRegisterMenuScreens(net.neoforged.neoforge.client.event.RegisterMenuScreensEvent event) {
		RegisterMenuScreensEvent.EVENT.invoker().registerMenuScreens((RegisterMenuScreensEvent) event);
	}

	@SubscribeEvent
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		RegisterBlockEntityRenderersEvent.EVENT.invoker().registerBlockEntityRenderers((RegisterBlockEntityRenderersEvent) event);
		RegisterEntityRenderersEvent.EVENT.invoker().registerEntityRenderers((RegisterEntityRenderersEvent) event);
		RegisterLecternItemRendererEvent.EVENT.invoker().registerBookRenderers(new RegisterLecternItemRendererEvent());
	}

	@SubscribeEvent
	public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
		RegisterParticleFactoriesEvent.EVENT.invoker().registerParticleFactories(new RegisterParticleFactoriesEventImpl(event));
	}

	@SubscribeEvent
	public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		RegisterLayerDefinitionsEvent.EVENT.invoker().registerModelLayers(event::registerLayerDefinition);
	}

	@SubscribeEvent
	public static void onRegisterEntityLayers(EntityRenderersEvent.AddLayers event) {
		RegisterCustomArmorRenderersEvent.EVENT.invoker().registerCustomArmorRenderers(new RegisterCustomArmorRenderersEvent());
	}

	@SubscribeEvent
	public static void onClientPreTick(ClientTickEvent.Pre event) {
		ClientTickEvents.START_TICK.invoker().startTick(Minecraft.getInstance());
	}

	@SubscribeEvent
	public static void onClientPostTick(ClientTickEvent.Post event) {
		ClientTickEvents.END_TICK.invoker().endTick(Minecraft.getInstance());
	}

	@SubscribeEvent
	public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
		if(SparkweaveApi.Client.LOG_MISSING_TRANSLATIONS) {
			event.registerReloadListener(new ResourceManagerReloadListener() {
				private final ResourceLocation ID = SparkweaveMod.id("translation_checker");

				@Override
				public String getName() {
					return ID.toString();
				}

				@Override
				public void onResourceManagerReload(ResourceManager resourceManager) {
					TranslationChecker.validate();
				}
			});
		}
	}

	@SubscribeEvent
	public static void onRenderWorld(RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
			DebugRenderer.render(event.getPoseStack(), event.getLevelRenderer().renderBuffers.bufferSource(), event.getCamera());
		}
	}
}
