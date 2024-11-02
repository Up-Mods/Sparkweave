package dev.upcraft.sparkweave.testmod.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.upcraft.sparkweave.api.client.render.CustomArmorRenderer;
import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import dev.upcraft.sparkweave.testmod.client.models.MageRobesModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class MageRobesRenderer implements CustomArmorRenderer {
	private static final ResourceLocation TEXTURE = SparkweaveTestmod.id("textures/entity/armor/mage_robes.png");
	private MageRobesModel<LivingEntity> model;

	@Override
	public void render(PoseStack matrices, MultiBufferSource bufferSource, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
		if(model == null)
			model = new MageRobesModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(MageRobesModel.MODEL_LAYER));

		contextModel.copyPropertiesTo(model);
		model.setAllVisible(true);
		model.openHood.visible = slot == EquipmentSlot.HEAD;
		model.closedHood.visible = false;
		model.cloak.visible = slot == EquipmentSlot.HEAD;
		model.garb.visible = slot == EquipmentSlot.CHEST;
		model.leftSleeve.visible = slot == EquipmentSlot.CHEST;
		model.rightSleeve.visible = slot == EquipmentSlot.CHEST;
		model.belt.visible = slot == EquipmentSlot.LEGS;
		model.leftShoe.visible = slot == EquipmentSlot.FEET;
		model.rightShoe.visible = slot == EquipmentSlot.FEET;

		CustomArmorRenderer.renderArmor(matrices, bufferSource, light, stack, model, TEXTURE);
	}
}
