package dev.upcraft.sparkweave.testmod.init;

import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import dev.upcraft.sparkweave.api.registry.item.ItemRegistryHandler;
import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import dev.upcraft.sparkweave.testmod.data.TestArmorMaterials;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;

public class TestItems {

	public static final ItemRegistryHandler ITEMS = RegistryHandler.items(SparkweaveTestmod.MODID);

	public static final RegistrySupplier<Item> TEST_ITEM = ITEMS.register("test_item", Item::new, new Item.Properties());
	public static final Holder<Item> TEST_ITEM_HOLDER_EARLY = TEST_ITEM.holder();

	public static final RegistrySupplier<Item> MAGE_HOOD = ITEMS.register("mage_hood", Item::new, () -> new Item.Properties().humanoidArmor(TestArmorMaterials.MAGE_ROBES, ArmorType.HELMET));
	public static final RegistrySupplier<Item> MAGE_ROBES = ITEMS.register("mage_robes", Item::new, () -> new Item.Properties().humanoidArmor(TestArmorMaterials.MAGE_ROBES, ArmorType.CHESTPLATE));
	public static final RegistrySupplier<Item> MAGE_LEGGINGS = ITEMS.register("mage_leggings", Item::new, () -> new Item.Properties().humanoidArmor(TestArmorMaterials.MAGE_ROBES, ArmorType.LEGGINGS));
	public static final RegistrySupplier<Item> MAGE_BOOTS = ITEMS.register("mage_boots", Item::new, () -> new Item.Properties().humanoidArmor(TestArmorMaterials.MAGE_ROBES, ArmorType.BOOTS));
}
