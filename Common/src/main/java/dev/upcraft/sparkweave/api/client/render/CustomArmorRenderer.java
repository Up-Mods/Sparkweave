package dev.upcraft.sparkweave.api.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.upcraft.sparkweave.api.client.armorrenderer.ArmorData;
import net.minecraft.client.model.EntityModel;
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

public abstract class CustomArmorRenderer<ENTITY extends LivingEntity, BASESTATE extends HumanoidRenderState, BASEMODEL extends EntityModel<BASESTATE>> {

	public abstract void extractRenderState(EquipmentSlot slot, ENTITY entity, BASESTATE baseState, ArmorData customState, float partialTicks, ItemModelResolver itemModelResolver);

	public abstract void submit(EquipmentLayerRenderer renderer, EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAssetId, BASEMODEL model, BASESTATE state, ArmorData armorData, EquipmentSlot slot, ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int outlineColor);

	@Nullable
	public abstract ResourceKey<EquipmentAsset> getOverrideEquipmentAssetId(EquipmentSlot slot, ENTITY entity, BASESTATE baseState, ArmorData customState);

	@FunctionalInterface
	public interface Factory<ENTITY extends LivingEntity, BASESTATE extends HumanoidRenderState, BASEMODEL extends EntityModel<BASESTATE>> {

		@Nullable CustomArmorRenderer<? extends ENTITY, ? extends BASESTATE, ? extends BASEMODEL> create(ENTITY entity, EntityRendererProvider.Context context);
	}
}
