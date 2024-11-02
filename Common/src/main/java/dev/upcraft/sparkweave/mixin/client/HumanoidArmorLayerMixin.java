package dev.upcraft.sparkweave.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.upcraft.sparkweave.api.client.event.CustomArmorRendererRegistryEvent;
import dev.upcraft.sparkweave.api.client.render.CustomArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin extends RenderLayer<LivingEntity, HumanoidModel<LivingEntity>> {
	public HumanoidArmorLayerMixin(RenderLayerParent<LivingEntity, HumanoidModel<LivingEntity>> parent) { super(parent); }

	@Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
	private void disableDefaultArmorRendererForCustomArmor(PoseStack poseStack, MultiBufferSource bufferSource, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> model, CallbackInfo info) {
		ItemStack stack = entity.getItemBySlot(slot);
		CustomArmorRenderer renderer = CustomArmorRendererRegistryEvent.get(stack.getItem());

		if(renderer != null) {
			renderer.render(poseStack, bufferSource, stack, entity, slot, light, getParentModel());
			info.cancel();
		}
	}
}
