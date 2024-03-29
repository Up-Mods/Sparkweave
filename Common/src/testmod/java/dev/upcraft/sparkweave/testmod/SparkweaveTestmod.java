package dev.upcraft.sparkweave.testmod;

import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.testmod.init.TestCreativeTabs;
import dev.upcraft.sparkweave.testmod.init.TestItems;

public class SparkweaveTestmod {

	public static final String MODID = "sparkweave_testmod";

	public static void init() {
		var registryService = RegistryService.get();

		TestItems.ITEMS.accept(registryService);
		TestCreativeTabs.TABS.accept(registryService);
	}
}
