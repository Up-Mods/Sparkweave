package dev.upcraft.sparkweave.testmod.data;

import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TestmodTags {

	public static class Items {
		public static final TagKey<Item> MAGE_ROBES_REPAIR_MATERIALS = TagKey.create(Registries.ITEM, SparkweaveTestmod.id("mage_robes_repair_materials"));
	}
}
