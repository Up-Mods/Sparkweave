package dev.upcraft.sparkweave.testmod.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.upcraft.sparkweave.api.client.armorrenderer.ArmorData;
import dev.upcraft.sparkweave.api.client.render.CustomHumanoidModelArmorRenderer;
import dev.upcraft.sparkweave.testmod.client.models.MageRobesModel;
import dev.upcraft.sparkweave.testmod.data.TestmodEquipmentAssets;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import org.jetbrains.annotations.Nullable;

public class MageRobesRenderer extends CustomHumanoidModelArmorRenderer<LivingEntity, HumanoidRenderState, HumanoidModel<HumanoidRenderState>, HumanoidRenderState, MageRobesModel<HumanoidRenderState>> {

	private final MageRobesModel<HumanoidRenderState> model;

	public MageRobesRenderer(LivingEntity entity, EntityRendererProvider.Context context) {
		this.model = new MageRobesModel<>(context.bakeLayer(MageRobesModel.MODEL_LAYER));
	}

	@Override
	protected HumanoidRenderState createRenderState() {
		return new HumanoidRenderState();
	}

	@Override
	protected MageRobesModel<HumanoidRenderState> getArmorModel(HumanoidRenderState humanoidRenderState, HumanoidRenderState renderState, ArmorData armorData, EquipmentSlot slot) {
		return this.model;
	}

	@Override
	protected void extractCustomRenderState(EquipmentSlot slot, LivingEntity entity, HumanoidRenderState baseState, HumanoidRenderState state, ArmorData armorData, float partialTicks, ItemModelResolver itemModelResolver) {

	}

	@Override
	protected void setupAnim(MageRobesModel<HumanoidRenderState> model, HumanoidRenderState baseState, HumanoidRenderState customState, ArmorData armorData, EquipmentSlot slot) {
		model.closedHood.visible = false;
		model.openHood.visible = slot == EquipmentSlot.HEAD;
		model.cloak.visible = slot == EquipmentSlot.HEAD;
		model.garb.visible = slot == EquipmentSlot.CHEST;
		model.leftSleeve.visible = slot == EquipmentSlot.CHEST;
		model.rightSleeve.visible = slot == EquipmentSlot.CHEST;
		model.belt.visible = slot == EquipmentSlot.LEGS;
		model.leftShoe.visible = slot == EquipmentSlot.FEET;
		model.rightShoe.visible = slot == EquipmentSlot.FEET;
	}

	@Override
	protected void renderArmorPiece(EquipmentLayerRenderer renderer, EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAssetId, MageRobesModel<HumanoidRenderState> model, HumanoidRenderState state, ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, EquipmentSlot slot, int lightCoords, int outlineColor, HumanoidRenderState baseState, ArmorData armorData) {
		renderer.renderLayers(layerType, equipmentAssetId, model, baseState, itemStack, poseStack, submitNodeCollector, lightCoords, outlineColor);
	}

	@Override
	public @Nullable ResourceKey<EquipmentAsset> getOverrideEquipmentAssetId(EquipmentSlot slot, LivingEntity entity, HumanoidRenderState baseState, ArmorData customState) {
		return TestmodEquipmentAssets.MAGE_ROBES;
	}

	public static class RenderState extends HumanoidRenderState {

	}
}
