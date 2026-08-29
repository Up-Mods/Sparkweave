package dev.upcraft.sparkweave.api.item;

import com.google.common.collect.Sets;
import dev.upcraft.sparkweave.api.platform.Services;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.item.CreativeTabFiller;
import dev.upcraft.sparkweave.platform.SparkweaveHelperService;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.Set;
import java.util.function.Supplier;

public class CreativeTabHelper {

	private static final SparkweaveHelperService HELPER = Services.getService(SparkweaveHelperService.class);

	@SafeVarargs
	public static void addRegistryEntries(CreativeModeTab.ItemDisplayParameters displayParameters, CreativeModeTab.Output collector, RegistryHandler<? extends ItemLike>... itemProviders) {
		Set<Object> seen = Sets.newIdentityHashSet(); // make sure to only look at each object once, even if we encounter it multiple times due to block-items

		for (RegistryHandler<? extends ItemLike> itemProvider : itemProviders) {
			itemProvider.stream().map(Supplier::get).forEach(registryObject -> {
				if (registryObject instanceof CreativeTabFiller filler) {
					if(seen.add(registryObject)) {
						filler.addItemsToTab(displayParameters, collector);
					}
				} else {
					var item = registryObject.asItem();
					if(item == Items.AIR || !seen.add(item)) {
						return;
					}

					if(item instanceof CreativeTabFiller filler) {
						filler.addItemsToTab(displayParameters, collector);
					}
					else {
						collector.accept(registryObject);
					}
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
