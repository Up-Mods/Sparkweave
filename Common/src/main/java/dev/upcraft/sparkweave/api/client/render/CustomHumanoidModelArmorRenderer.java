package dev.upcraft.sparkweave.api.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.client.armorrenderer.ArmorData;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;

public abstract class CustomHumanoidModelArmorRenderer<ENTITY extends LivingEntity, BASESTATE extends HumanoidRenderState, BASEMODEL extends HumanoidModel<BASESTATE>, CUSTOMSTATE extends HumanoidRenderState, ARMORMODEL extends HumanoidModel<CUSTOMSTATE>> extends CustomArmorRenderer<ENTITY, BASESTATE, BASEMODEL> {

	private static final ContextKey<HumanoidRenderState> CUSTOM_RENDER_STATE = new ContextKey<>(SparkweaveMod.id("custom_render_state"));

	protected abstract CUSTOMSTATE createRenderState();

	protected abstract ARMORMODEL getArmorModel(BASESTATE basestate, CUSTOMSTATE customstate, ArmorData armorData, EquipmentSlot slot);

	@SuppressWarnings("unchecked")
	@Override
	public final void extractRenderState(EquipmentSlot slot, ENTITY entity, BASESTATE baseState, ArmorData armorData, ItemModelResolver itemModelResolver) {
		var customState = (CUSTOMSTATE) armorData.getCustomData(CUSTOM_RENDER_STATE);
		if(customState == null) {
			customState = createRenderState();
			armorData.setCustomData(CUSTOM_RENDER_STATE, customState);
		}

		extractCustomRenderState(slot, entity, baseState, customState, armorData, itemModelResolver);
	}

	protected abstract void extractCustomRenderState(EquipmentSlot slot, ENTITY entity, BASESTATE baseState, CUSTOMSTATE customstate, ArmorData armorData, ItemModelResolver itemModelResolver);

	@SuppressWarnings("unchecked")
	@Override
	public final void submit(EquipmentLayerRenderer renderer, EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAssetId, BASEMODEL baseModel, BASESTATE baseState, ArmorData armorData, EquipmentSlot slot, ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int outlineColor) {
		var customState = (CUSTOMSTATE) armorData.getCustomData(CUSTOM_RENDER_STATE);
		var armorModel = getArmorModel(baseState, customState, armorData, slot);
		armorModel.resetPose();
		baseModel.sparkweave$copyPropertiesTo(armorModel);
		this.setupAnim(armorModel, baseState, customState, armorData, slot);
		this.renderArmorPiece(renderer, layerType, equipmentAssetId, armorModel, customState, itemStack, poseStack, submitNodeCollector, slot, lightCoords, outlineColor, baseState, armorData);
	}

	protected abstract void setupAnim(ARMORMODEL model, BASESTATE baseState, CUSTOMSTATE customState, ArmorData armorData, EquipmentSlot slot);

	protected abstract void renderArmorPiece(EquipmentLayerRenderer renderer, EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAssetId, ARMORMODEL model, CUSTOMSTATE state, ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, EquipmentSlot slot, int lightCoords, int outlineColor, BASESTATE baseState, ArmorData armorData);
}
