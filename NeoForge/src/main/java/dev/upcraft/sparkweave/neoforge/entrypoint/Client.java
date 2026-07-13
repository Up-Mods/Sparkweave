package dev.upcraft.sparkweave.neoforge.entrypoint;

import com.mojang.brigadier.CommandDispatcher;
import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.SparkweaveApi;
import dev.upcraft.sparkweave.api.client.command.ClientCommandSource;
import dev.upcraft.sparkweave.api.client.event.*;
import dev.upcraft.sparkweave.api.client.event.RegisterMenuScreensEvent;
import dev.upcraft.sparkweave.api.client.event.RegisterParticleProvidersEvent;
import dev.upcraft.sparkweave.client.debug.SparkweaveDebugRenderer;
import dev.upcraft.sparkweave.neoforge.impl.registry.RegisterParticleProvidersEventImpl;
import dev.upcraft.sparkweave.validation.TranslationChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;

@EventBusSubscriber(modid = SparkweaveMod.MODID, value = Dist.CLIENT)
public class Client {

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
	public static void onRegisterParticles(net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent event) {
		RegisterParticleProvidersEvent.EVENT.invoker().registerParticleFactories(new RegisterParticleProvidersEventImpl(event));
	}

	@SubscribeEvent
	public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		RegisterLayerDefinitionsEvent.EVENT.invoker().registerModelLayers(event::registerLayerDefinition);
	}

	@SubscribeEvent
	public static void onRegisterEntityLayers(EntityRenderersEvent.AddLayers event) {
		RegisterCustomArmorRenderersEvent.EVENT.invoker().registerCustomArmorRenderers(new RegisterCustomArmorRenderersEvent());
	}

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
		ClientCommandEvents.REGISTER.invoker().registerClientCommands((CommandDispatcher<ClientCommandSource>)(Object) event.getDispatcher(), event.getBuildContext());
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
	public static void onRegisterReloadListeners(AddClientReloadListenersEvent event) {
		if(SparkweaveApi.Client.LOG_MISSING_TRANSLATIONS) {
			event.addListener(SparkweaveMod.id("translation_checker"), (ResourceManagerReloadListener) _ -> TranslationChecker.validate());
		}
	}

	@SubscribeEvent
	public static void onRenderWorld(RenderLevelStageEvent.AfterTranslucentBlocks event) {
		SparkweaveDebugRenderer.render(event.getPoseStack(), event.getLevelRenderer().renderBuffers.bufferSource(), event.getLevelRenderState());
	}

	@SubscribeEvent
	public static void registerRangeSelectItemModelProperties(RegisterRangeSelectItemModelPropertyEvent event) {
		RegisterItemModelPropertiesEvent.RANGED.invoker().registerProperties(event::register);
	}

	@SubscribeEvent
	public static void registerConditionalSelectItemModelProperties(RegisterConditionalItemModelPropertyEvent event) {
		RegisterItemModelPropertiesEvent.CONDITIONAL.invoker().registerProperties(event::register);
	}

	@SubscribeEvent
	public static void registerSpecialItemModelProperties(RegisterSelectItemModelPropertyEvent event) {
		RegisterItemModelPropertiesEvent.SELECT.invoker().registerProperties(event::register);
	}
}
