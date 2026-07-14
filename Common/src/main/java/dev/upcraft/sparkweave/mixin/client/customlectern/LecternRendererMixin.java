package dev.upcraft.sparkweave.mixin.client.customlectern;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.upcraft.sparkweave.api.client.render.LecternItemRenderer;
import dev.upcraft.sparkweave.client.event.LecternItemRendererRegistryImpl;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.object.book.BookModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.LecternRenderer;
import net.minecraft.client.renderer.blockentity.state.LecternRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LecternRenderer.class)
public abstract class LecternRendererMixin implements BlockEntityRenderer<LecternBlockEntity, LecternRenderState> {

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Inject(method = "extractRenderState(Lnet/minecraft/world/level/block/entity/LecternBlockEntity;Lnet/minecraft/client/renderer/blockentity/state/LecternRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V", at = @At("RETURN"))
	private void extractCustomRenderState(LecternBlockEntity blockEntity, LecternRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress, CallbackInfo ci) {
		if(blockEntity.hasBook()) {
			var book = blockEntity.getBook();

			LecternItemRenderer customRenderer = LecternItemRendererRegistryImpl.get(book);
			if(customRenderer != null) {
				var customState = state.sparkweave$getData(LecternItemRendererRegistryImpl.ITEM_RENDERER_DATA_KEY);
				if(customState == null || customState.getClass() != customRenderer.getRenderStateType()) {
					customState = customRenderer.createRenderState();
					state.sparkweave$setData(LecternItemRendererRegistryImpl.ITEM_RENDERER_DATA_KEY, customState);
				}
				customRenderer.extractRenderState(blockEntity, state, customState, partialTicks, cameraPosition, breakProgress);
				state.sparkweave$setData(LecternItemRendererRegistryImpl.ITEM_RENDERER_KEY, customRenderer);
			}
			else {
				state.sparkweave$setData(LecternItemRendererRegistryImpl.ITEM_RENDERER_KEY, null);
				state.sparkweave$setData(LecternItemRendererRegistryImpl.ITEM_RENDERER_DATA_KEY, null);
			}
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@WrapOperation(method = "submit(Lnet/minecraft/client/renderer/blockentity/state/LecternRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;IIILnet/minecraft/client/resources/model/sprite/SpriteId;Lnet/minecraft/client/resources/model/sprite/SpriteGetter;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"))
	private void renderCustom(SubmitNodeCollector submitNodeCollector, Model<BookModel.State> bookModel, Object renderStateObj, PoseStack poseStack, int light, int overlay, int tintColor, SpriteId bookTexture, SpriteGetter spriteGetter, int outlineColor, ModelFeatureRenderer.CrumblingOverlay breakProgress, Operation<Void> original, LecternRenderState state, PoseStack unusedPoseStack, SubmitNodeCollector unusedSubmitNodeCollector,  CameraRenderState camera) {
		LecternItemRenderer customRenderer = state.sparkweave$getData(LecternItemRendererRegistryImpl.ITEM_RENDERER_KEY);
		if(customRenderer != null) {
			var customRenderState = state.sparkweave$getData(LecternItemRendererRegistryImpl.ITEM_RENDERER_DATA_KEY);
			customRenderer.submit(state, customRenderState, poseStack, submitNodeCollector, camera);
			return;
		}

		original.call(submitNodeCollector, bookModel, renderStateObj, poseStack, light, overlay, tintColor, bookTexture, spriteGetter, outlineColor, breakProgress);
	}
}
