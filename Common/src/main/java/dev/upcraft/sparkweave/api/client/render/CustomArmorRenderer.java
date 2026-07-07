package dev.upcraft.sparkweave.api.client.render;

import dev.upcraft.sparkweave.mixin.client.customarmor.ArmorData;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public abstract class CustomArmorRenderer<ENTITY extends LivingEntity, BASESTATE extends HumanoidRenderState, BASEMODEL extends EntityModel<BASESTATE>, CUSTOMSTATE extends EntityRenderState, ARMORMODEL extends EntityModel<CUSTOMSTATE>> implements RenderLayerParent<CUSTOMSTATE, ARMORMODEL> {

	public abstract void extractRenderState(EquipmentSlot slot, ENTITY entity, BASESTATE baseState, ArmorData state);

	public abstract void submit();

	@FunctionalInterface
	public interface Factory<ENTITY extends LivingEntity, BASESTATE extends HumanoidRenderState, BASEMODEL extends EntityModel<BASESTATE>, CUSTOMSTATE extends EntityRenderState, ARMORMODEL extends EntityModel<CUSTOMSTATE>> {

		@Nullable CustomArmorRenderer<? extends ENTITY, ? extends BASESTATE, ? extends BASEMODEL, ? extends CUSTOMSTATE, ? extends ARMORMODEL> create(ENTITY entity, EntityRendererProvider.Context context, RenderLayerParent<BASESTATE, BASEMODEL> renderer);
	}
}
