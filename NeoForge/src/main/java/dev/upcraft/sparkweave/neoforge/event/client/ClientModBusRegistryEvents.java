package dev.upcraft.sparkweave.neoforge.event.client;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.client.event.*;
import dev.upcraft.sparkweave.client.event.RegisterItemPropertiesEventImpl;
import dev.upcraft.sparkweave.neoforge.impl.registry.RegisterParticleFactoriesEventImpl;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = SparkweaveMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ClientModBusRegistryEvents {

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

}
