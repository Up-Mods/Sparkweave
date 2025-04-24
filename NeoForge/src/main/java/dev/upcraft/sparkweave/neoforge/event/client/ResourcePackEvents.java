package dev.upcraft.sparkweave.neoforge.event.client;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.SparkweaveApi;
import dev.upcraft.sparkweave.validation.TranslationChecker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

@EventBusSubscriber(modid = SparkweaveMod.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ResourcePackEvents {

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
}
