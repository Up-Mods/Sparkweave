package dev.upcraft.sparkweave.testmod.init;

import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class TestItems {

	public static final RegistryHandler<Item> ITEMS = RegistryHandler.create(Registries.ITEM, SparkweaveTestmod.MODID);

	public static final RegistrySupplier<Item> TEST_ITEM = ITEMS.register("test_item", () -> new Item(new Item.Properties()));
	public static final Holder<Item> TEST_ITEM_HOLDER_EARLY = TEST_ITEM.holder();
}
