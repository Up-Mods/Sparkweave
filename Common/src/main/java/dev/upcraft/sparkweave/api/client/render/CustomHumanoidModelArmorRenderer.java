package dev.upcraft.sparkweave.api.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public abstract class CustomHumanoidModelArmorRenderer<E extends LivingEntity, M extends HumanoidModel<E>, A extends HumanoidModel<E>> extends CustomArmorRenderer<E, M> {

	protected CustomHumanoidModelArmorRenderer() {
	}

	protected abstract void setPartVisibility(A model, M contextModel, E entity, ItemStack stack, EquipmentSlot slot);

	protected abstract A getArmorModel(E entity, ItemStack stack, EquipmentSlot slot);

	@Override
	public final void render(PoseStack matrices, MultiBufferSource bufferSource, ItemStack stack, E entity, EquipmentSlot slot, int light, M contextModel) {
		A armorModel = this.getArmorModel(entity, stack, slot);
		contextModel.copyPropertiesTo(armorModel);
		armorModel.setAllVisible(false);
		this.setPartVisibility(armorModel, contextModel, entity, stack, slot);

		int dyeColor = stack.is(ItemTags.DYEABLE) ? FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(stack, -6265536)) : -1;

		renderModelPart(matrices, bufferSource, stack, entity, slot, light, dyeColor, contextModel, armorModel);
	}

	protected abstract ResourceLocation getTexture(E entity, ItemStack stack, EquipmentSlot slot);

	protected void renderModelPart(PoseStack matrices, MultiBufferSource bufferSource, ItemStack stack, E entity, EquipmentSlot slot, int light, int dyeColor, M contextModel, A armorModel) {
		VertexConsumer buffer = getArmorBuffer(bufferSource, stack, getTexture(entity, stack, slot));
		armorModel.renderToBuffer(matrices, buffer, light, OverlayTexture.NO_OVERLAY, dyeColor);
	}

	protected static VertexConsumer getArmorBuffer(MultiBufferSource bufferSource, ItemStack stack, ResourceLocation texture) {
		return ItemRenderer.getArmorFoilBuffer(bufferSource, RenderType.armorCutoutNoCull(texture), stack.hasFoil());
	}
}
