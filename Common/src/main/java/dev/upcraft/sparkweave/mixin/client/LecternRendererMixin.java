package dev.upcraft.sparkweave.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.upcraft.sparkweave.client.event.LecternItemRendererRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.LecternRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LecternRenderer.class)
public class LecternRendererMixin {
	@Inject(method = "render(Lnet/minecraft/world/level/block/entity/LecternBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
			at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V"),
			cancellable = true
	)
	private void renderCustomBookInstead(LecternBlockEntity lecternBlockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, CallbackInfo info, @Local BlockState blockState) {
		ItemStack itemStack = lecternBlockEntity.hasBook() ? lecternBlockEntity.getBook() : ItemStack.EMPTY;

		LecternItemRendererRegistry.get(itemStack.getItem()).ifPresent(lecternItemRenderer -> {
			poseStack.pushPose();
			poseStack.translate(0.5f, 1.0625f, 0.5f);
			poseStack.mulPose(Axis.YP.rotationDegrees(-blockState.getValue(LecternBlock.FACING).getClockWise().toYRot()));
			lecternItemRenderer.renderBook(lecternBlockEntity, blockState, itemStack, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
			poseStack.popPose();
			info.cancel();
		});
	}
}
