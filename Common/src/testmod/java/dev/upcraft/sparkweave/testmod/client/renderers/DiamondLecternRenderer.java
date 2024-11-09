package dev.upcraft.sparkweave.testmod.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.upcraft.sparkweave.api.client.render.LecternItemRenderer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DiamondLecternRenderer extends LecternItemRenderer {
	public DiamondLecternRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void renderBook(LecternBlockEntity lecternBlockEntity, BlockState blockState, ItemStack itemStack, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		poseStack.pushPose();

		poseStack.translate(0.2, -0.085, 0);
		poseStack.mulPose(Axis.YP.rotationDegrees(90));
		poseStack.mulPose(Axis.XN.rotationDegrees(67.5f));

		context.getItemRenderer().renderStatic(itemStack, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, lecternBlockEntity.getLevel(), 0);

		poseStack.popPose();
	}

	@Nullable
	@Override
	public Model getBookModel() {
		return null;
	}
}
