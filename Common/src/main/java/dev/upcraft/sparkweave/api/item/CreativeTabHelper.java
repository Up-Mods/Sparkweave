package dev.upcraft.sparkweave.api.item;

import dev.upcraft.sparkweave.api.platform.Services;
import dev.upcraft.sparkweave.api.platform.services.SparkweaveHelperService;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.item.CreativeTabFiller;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class CreativeTabHelper {

	private static final SparkweaveHelperService HELPER = Services.getService(SparkweaveHelperService.class);

	@SafeVarargs
	public static void addRegistryEntries(CreativeModeTab.ItemDisplayParameters displayParameters, CreativeModeTab.Output collector, RegistryHandler<? extends ItemLike>... itemProviders) {
		for (RegistryHandler<? extends ItemLike> itemProvider : itemProviders) {
			itemProvider.stream().map(Supplier::get).forEach(registryObject -> {
				if (registryObject instanceof CreativeTabFiller filler) {
					filler.addItemsToTab(displayParameters, collector);
				} else if (registryObject.asItem() instanceof CreativeTabFiller filler) {
					filler.addItemsToTab(displayParameters, collector);
				} else {
					collector.accept(registryObject);
				}
			});
		}
	}

	public static CreativeModeTab.Builder newBuilder(Component title) {
		return HELPER.newCreativeTabBuilder(title);
	}

	public static CreativeModeTab.Builder newBuilder(Identifier id) {
		return newBuilder(Component.translatable(Util.makeDescriptionId("itemGroup", id)));
	}

	public static CreativeModeTab.Builder newBuilder(ResourceKey<CreativeModeTab> id) {
		return newBuilder(id.identifier());
	}
}
