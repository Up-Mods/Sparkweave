package dev.upcraft.test.sparkweave;

import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.api.registry.RegistryService;
import dev.upcraft.test.sparkweave.init.TestCreativeTabs;
import dev.upcraft.test.sparkweave.init.TestItems;
import net.fabricmc.api.ModInitializer;

@CalledByReflection
public class SparkweaveTestMod implements ModInitializer {
    public static final String MODID = "sparkweave-testmod";

    @Override
	public void onInitialize() {
        var service = RegistryService.get();
        TestItems.ITEMS.accept(service);
        TestCreativeTabs.TABS.accept(service);
	}
}
