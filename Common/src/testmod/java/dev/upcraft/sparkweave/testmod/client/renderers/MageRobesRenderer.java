//package dev.upcraft.sparkweave.testmod.client.renderers;
//
//import dev.upcraft.sparkweave.api.client.render.CustomHumanoidModelArmorRenderer;
//import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
//import dev.upcraft.sparkweave.testmod.client.models.MageRobesModel;
//import net.minecraft.client.model.HumanoidModel;
//import net.minecraft.client.renderer.entity.EntityRendererProvider;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.ItemStack;
//
//public class MageRobesRenderer extends CustomHumanoidModelArmorRenderer<LivingEntity, HumanoidModel<LivingEntity>, MageRobesModel<LivingEntity>> {
//	private static final ResourceLocation TEXTURE = SparkweaveTestmod.id("textures/entity/armor/mage_robes.png");
//
//	private final MageRobesModel<LivingEntity> model;
//
//	public MageRobesRenderer(EntityRendererProvider.Context context) {
//		this.model = new MageRobesModel<>(context.bakeLayer(MageRobesModel.MODEL_LAYER));
//	}
//
//	@Override
//	protected void setPartVisibility(MageRobesModel<LivingEntity> model, HumanoidModel<LivingEntity> contextModel, LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
//		model.setAllVisible(true);
//		model.closedHood.visible = false;
//		model.openHood.visible = slot == EquipmentSlot.HEAD;
//		model.cloak.visible = slot == EquipmentSlot.HEAD;
//		model.garb.visible = slot == EquipmentSlot.CHEST;
//		model.leftSleeve.visible = slot == EquipmentSlot.CHEST;
//		model.rightSleeve.visible = slot == EquipmentSlot.CHEST;
//		model.belt.visible = slot == EquipmentSlot.LEGS;
//		model.leftShoe.visible = slot == EquipmentSlot.FEET;
//		model.rightShoe.visible = slot == EquipmentSlot.FEET;
//	}
//
//	@Override
//	protected MageRobesModel<LivingEntity> getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
//		return this.model;
//	}
//
//	@Override
//	protected ResourceLocation getTexture(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
//		return TEXTURE;
//	}
//}
