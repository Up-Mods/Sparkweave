package dev.upcraft.sparkweave.testmod.init;

import dev.upcraft.sparkweave.api.item.CreativeTabHelper;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;

public class TestCreativeTabs {

	public static final RegistryHandler<CreativeModeTab> TABS = RegistryHandler.create(Registries.CREATIVE_MODE_TAB, SparkweaveTestmod.MODID);

	public static final RegistrySupplier<CreativeModeTab> ITEMS = TABS.register("items", () -> CreativeTabHelper.newBuilder(SparkweaveTestmod.id("items"))
		.icon(() -> TestItems.TEST_ITEM.get().getDefaultInstance())
		.displayItems((itemDisplayParameters, output) -> CreativeTabHelper.addRegistryEntries(itemDisplayParameters, output, TestItems.ITEMS, TestBlocks.BLOCKS))
		.build()
	);
}
