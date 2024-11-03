package dev.upcraft.sparkweave.api.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class CustomArmorRenderer<E extends LivingEntity, M extends EntityModel<E>> {

	public abstract void render(PoseStack matrices, MultiBufferSource bufferSource, ItemStack stack, E entity, EquipmentSlot slot, int light, M contextModel);

	@FunctionalInterface
	public interface Factory<E extends LivingEntity, M extends EntityModel<E>> {

		@Nullable CustomArmorRenderer<? extends E, ? extends M> create(LivingEntity entity, EntityRendererProvider.Context context, RenderLayerParent<E, M> renderer);
	}

}
