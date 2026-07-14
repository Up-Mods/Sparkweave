package dev.upcraft.sparkweave.mixin.client.armorrenderer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.upcraft.sparkweave.api.client.armorrenderer.ArmorData;
import dev.upcraft.sparkweave.api.client.render.CustomArmorRenderer;
import dev.upcraft.sparkweave.client.event.ArmorRendererRegistry;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<BASESTATE extends HumanoidRenderState, BASEMODEL extends HumanoidModel<BASESTATE>, CUSTOMSTATE extends HumanoidRenderState, ARMORMODEL extends EntityModel<CUSTOMSTATE>> extends RenderLayer<BASESTATE, BASEMODEL> {

	private HumanoidArmorLayerMixin(RenderLayerParent<BASESTATE, BASEMODEL> parent) {
		super(parent);
		throw new UnsupportedOperationException();
	}

	@ModifyExpressionValue(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;shouldRender(Lnet/minecraft/world/item/equipment/Equippable;Lnet/minecraft/world/entity/EquipmentSlot;)Z"))
	private boolean shouldRenderCustomArmor(boolean original, @Local(argsOnly = true) EquipmentSlot slot, @Local(argsOnly = true) BASESTATE state, @Share("customRenderData") LocalRef<ArmorData> customRenderData) {
		var ctxKey = ArmorRendererRegistry.ARMOR_CONTEXT_KEYS.get(slot);
		var renderData = state.sparkweave$getData(ctxKey);

		customRenderData.set(renderData);

		return original || renderData != null;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@WrapOperation(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer;renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;II)V"))
	private <S> void renderArmor(EquipmentLayerRenderer instance, EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAssetId, Model<? super S> model, S state, ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int outlineColor, Operation<Void> original, @Local(argsOnly = true) EquipmentSlot slot, @Share("customRenderData") LocalRef<ArmorData> customRenderData) {
		var customState = customRenderData.get();
		if(customState != null) {
			CustomArmorRenderer renderer = customState.getCustomRenderer();
			if(renderer != null) {
				renderer.submit(instance, layerType, Objects.requireNonNullElse(customState.getOverrideEquipmentAsset(), equipmentAssetId), (BASEMODEL) model, (BASESTATE) state, customState, slot, itemStack, poseStack, submitNodeCollector, lightCoords, outlineColor);
				return;
			}
		}

		original.call(instance, layerType, equipmentAssetId, model, state, itemStack, poseStack, submitNodeCollector, lightCoords, outlineColor);
	}
}
