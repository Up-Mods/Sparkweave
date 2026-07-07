package dev.upcraft.sparkweave.mixin.client.customarmor;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<ENTITY extends LivingEntity, BASESTATE extends HumanoidRenderState, BASEMODEL extends HumanoidModel<BASESTATE>, CUSTOMSTATE extends HumanoidRenderState, ARMORMODEL extends EntityModel<CUSTOMSTATE>> extends RenderLayer<BASESTATE, BASEMODEL> {

	private HumanoidArmorLayerMixin(RenderLayerParent<BASESTATE, BASEMODEL> parent) {
		super(parent);
		throw new UnsupportedOperationException();
	}

	//FIXME
//	@ModifyExpressionValue(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;shouldRender(Lnet/minecraft/world/item/equipment/Equippable;Lnet/minecraft/world/entity/EquipmentSlot;)Z"))
//	private boolean shouldRenderCustomArmor(boolean original, @Local(argsOnly = true) EquipmentSlot slot, @Local(argsOnly = true) BASESTATE state, @Share("customRenderData") LocalRef<Pair<CustomArmorRenderer<ENTITY, BASESTATE, BASEMODEL, CUSTOMSTATE, ARMORMODEL>, Object>> customRenderData) {
//		var ctxKey = ArmorRendererRegistry.ARMOR_CONTEXT_KEYS.get(slot);
//		var renderData = state.sparkweave$getData(ctxKey);
//
//		return original || renderData != null;
//	}
//
//
//	@WrapOperation(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer;renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;II)V"))
//	private void renderArmor(EquipmentLayerRenderer instance, EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAssetId, ARMORMODEL model, BASESTATE state, ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int outlineColor, Operation<Void> original, @Local(argsOnly = true) EquipmentSlot slot, @Share("customRenderData") LocalRef<Pair<CustomArmorRenderer<ENTITY, BASESTATE, BASEMODEL, CUSTOMSTATE, ARMORMODEL>, Object>> customRenderData) {
//		var customData = customRenderData.get();
//		if(customData != null) {
//			customData.getFirst().submit();
//			return;
//		}
//		original.call(instance, layerType, equipmentAssetId, model, state, itemStack, poseStack, submitNodeCollector, lightCoords, outlineColor);
//	}
}
