package dev.upcraft.sparkweave.testmod.data;

import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class TestmodEnchantments {

	public static final ResourceKey<Enchantment> TEST_ENCHANTMENT = ResourceKey.create(Registries.ENCHANTMENT, SparkweaveTestmod.id("test_enchantment"));
}
