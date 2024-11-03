package dev.upcraft.sparkweave.neoforge.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.upcraft.sparkweave.api.client.render.RenderLayerExtensions;
import dev.upcraft.sparkweave.client.event.ArmorRendererRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

	private HumanoidArmorLayerMixin(RenderLayerParent<T, M> parent) {
		super(parent);
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Inject(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"), cancellable = true)
	private void disableDefaultArmorRendererForCustomArmor(PoseStack poseStack, MultiBufferSource bufferSource, T entity, EquipmentSlot slot, int packedLight, A p_model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci, @Local ItemStack armorStack) {
		EquipmentSlot itemSlot = armorStack.getEquipmentSlot();
		if (itemSlot == slot || (itemSlot == null && (!(armorStack.getItem() instanceof Equipable equipable) || equipable.getEquipmentSlot() == slot))) {
			ArmorRendererRegistry.get(((RenderLayerExtensions<T, M>) this).sparkweave$getParent(), entity, armorStack).ifPresent(renderer -> {
				renderer.render(poseStack, bufferSource, armorStack, entity, slot, packedLight, this.getParentModel());
				ci.cancel();
			});
		}
	}
}
