package dev.upcraft.sparkweave.mixin.client.customarmor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("deprecation")
@Mixin(HumanoidMobRenderer.class)
public abstract class HumanoidMobRendererMixin<T extends Mob, S extends HumanoidRenderState, M extends HumanoidModel<S>> extends AgeableMobRenderer<T, S, M> {

	private HumanoidMobRendererMixin(EntityRendererProvider.Context context, M adultModel, M babyModel, float shadow) {
		super(context, adultModel, babyModel, shadow);
		throw new UnsupportedOperationException();
	}

//	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Mob;Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;F)V", at = @At("RETURN"))
//	private void extractCustomRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
//		for(var slot : ArmorRendererRegistry.ARMOR_SLOTS) {
//			var stack = entity.getItemBySlot(slot);
//			var dataKey = ArmorRendererRegistry.ARMOR_CONTEXT_KEYS.get(slot);
//
//			if(stack.has(DataComponents.EQUIPPABLE)) {
//				var renderer = ArmorRendererRegistry.get(this, entity, stack).orElse(null);
//				var data = state.sparkweave$getData(dataKey);
//				if(renderer != null) {
//					if(data == null) {
//						data = new ArmorData();
//						state.sparkweave$setData(dataKey, data);
//					}
//
//					if(!ItemStack.matches(data.getStack(), stack)) {
//						data.clear();
//						data.setStack(stack.copy());
//					}
//					renderer.extractRenderState(slot, entity, state, data);
//				}
//			}
//			else {
//				state.sparkweave$setData(dataKey, null);
//			}
//		}
//	}
}
