package dev.upcraft.sparkweave.api.item;

import dev.upcraft.sparkweave.api.platform.Services;
import dev.upcraft.sparkweave.api.platform.services.SparkweaveHelperService;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.item.CreativeTabFiller;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

public class CreativeTabHelper {

	private static final SparkweaveHelperService HELPER = Services.getService(SparkweaveHelperService.class);
	private static final RandomSource RANDOM_SOURCE = RandomSource.create();

	public static <T extends ItemLike> void addRegistryEntries(CreativeModeTab.ItemDisplayParameters displayParameters, CreativeModeTab.Output collector, RegistryHandler<T> itemProvider) {
		itemProvider.stream().forEach(supplier -> {

			var registryObject = supplier.get();

			if (registryObject instanceof CreativeTabFiller filler) {
				filler.addItemsToTab(displayParameters, collector, RANDOM_SOURCE);
			} else {
				collector.accept(registryObject);
			}
		});
	}

	public static RandomSource getRandom() {
		return RANDOM_SOURCE;
	}

	public static CreativeModeTab.Builder newBuilder(Component title) {
		return HELPER.newCreativeTabBuilder(title);
	}

	public static CreativeModeTab.Builder newBuilder(ResourceLocation id) {
		return newBuilder(Component.translatable(Util.makeDescriptionId("itemGroup", id)));
	}

	public static CreativeModeTab.Builder newBuilder(ResourceKey<CreativeModeTab> id) {
		return newBuilder(id.location());
	}
}
