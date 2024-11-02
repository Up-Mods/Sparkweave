package dev.upcraft.sparkweave.testmod;

import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.testmod.init.TestCreativeTabs;
import dev.upcraft.sparkweave.testmod.init.TestItems;
import dev.upcraft.sparkweave.testmod.init.TestStatusEffects;
import net.minecraft.resources.ResourceLocation;

public class SparkweaveTestmod implements MainEntryPoint {

	public static final String MODID = "sparkweave_testmod";

	@Override
	public void onInitialize(ModContainer mod) {
		var registryService = RegistryService.get();

		TestItems.ITEMS.accept(registryService);
		TestCreativeTabs.TABS.accept(registryService);
		TestStatusEffects.STATUS_EFFECTS.accept(registryService);
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}
}
