package dev.upcraft.sparkweave.testmod.datagen.common;

import dev.upcraft.sparkweave.api.datagen.provider.SparkweaveEnchantmentProvider;
import dev.upcraft.sparkweave.testmod.data.TestmodEnchantments;
import net.minecraft.core.HolderGetter;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.level.block.Block;

public class TestmodEnchantmentProvider extends SparkweaveEnchantmentProvider {

	@Override
	protected void generateEnchantments(Context ctx, HolderGetter<DamageType> damageTypes, HolderGetter<Enchantment> enchantments, HolderGetter<Item> items, HolderGetter<Block> blocks) {
		ctx.register(TestmodEnchantments.TEST_ENCHANTMENT, Enchantment.enchantment(
			Enchantment.definition(
				items.getOrThrow(ItemTags.EQUIPPABLE_ENCHANTABLE),
				10,
				4,
				Enchantment.dynamicCost(1, 11),
				Enchantment.dynamicCost(12, 11),
				3,
				EquipmentSlotGroup.HAND
			)
		).withEffect(EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP), "Test Enchantment", "Debug Enchantment to test Sparkweave Data generation APIs");
	}
}
