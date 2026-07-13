package dev.upcraft.sparkweave.testmod.data;

import com.google.common.collect.Maps;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.Map;

public class TestArmorMaterials {

	public static final ArmorMaterial MAGE_ROBES = new ArmorMaterial(0, makeDefense(1, 2, 3, 1, 3), 25, SoundEvents.ARMOR_EQUIP_GENERIC, 0.0F, 0.01F, TestmodTags.Items.MAGE_ROBES_REPAIR_MATERIALS, TestmodEquipmentAssets.MAGE_ROBES);

	private static Map<ArmorType, Integer> makeDefense(int boots, int legs, int chest, int helm, int body) {
		return Maps.newEnumMap(
			Map.of(ArmorType.BOOTS, boots, ArmorType.LEGGINGS, legs, ArmorType.CHESTPLATE, chest, ArmorType.HELMET, helm, ArmorType.BODY, body)
		);
	}
}
