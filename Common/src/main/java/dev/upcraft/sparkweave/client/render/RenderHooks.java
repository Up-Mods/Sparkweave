package dev.upcraft.sparkweave.client.render;

import dev.upcraft.sparkweave.api.client.armorrenderer.ArmorData;
import dev.upcraft.sparkweave.client.event.ArmorRendererRegistry;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class RenderHooks {

	public static <E extends LivingEntity, S extends HumanoidRenderState, M extends EntityModel<S>> void extractHumanoidRenderState(E entity, S state, float partialTicks, ItemModelResolver itemModelResolver) {
		for(var slot : ArmorRendererRegistry.ARMOR_SLOTS) {
			var stack = entity.getItemBySlot(slot);
			var dataKey = ArmorRendererRegistry.ARMOR_CONTEXT_KEYS.get(slot);

			if(stack.has(DataComponents.EQUIPPABLE)) {
				var data = state.sparkweave$getData(dataKey);

				var renderer = ArmorRendererRegistry.get(entity, stack).orElse(null);
				if(renderer != null) {
					if(data == null) {
						data = new ArmorData();
						state.sparkweave$setData(dataKey, data);
					}

					if(!ItemStack.matches(data.getItemStack(), stack)) {
						data.clear();
						data.setStack(stack.copy());
						data.setCustomRenderer(renderer);
					}
					data.setOverrideEquipmentAsset(renderer.getOverrideEquipmentAssetId(slot, entity, state, data));
					renderer.extractRenderState(slot, entity, state, data, partialTicks, itemModelResolver);
					continue;
				}
			}

			state.sparkweave$setData(dataKey, null);
		}
	}
}
