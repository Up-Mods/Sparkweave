package dev.upcraft.sparkweave.testmod.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.upcraft.sparkweave.api.client.render.LecternItemRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.LecternRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class DiamondLecternRenderer extends LecternItemRenderer<DiamondLecternRenderer.RenderState> {

	private final ItemModelResolver itemModelResolver;

	public DiamondLecternRenderer(BlockEntityRendererProvider.Context context) {
		super(RenderState.class);
		itemModelResolver = context.itemModelResolver();
	}

	@Override
	public RenderState createRenderState() {
		return new RenderState();
	}

	@Override
	public void extractRenderState(LecternBlockEntity blockEntity, LecternRenderState baseState, RenderState customState, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		var seed = (int) blockEntity.getBlockPos().asLong();
		itemModelResolver.updateForTopItem(customState.stack, blockEntity.getBook(), ItemDisplayContext.FIXED, blockEntity.getLevel(), blockEntity.sparkweave$asItemOwner(), seed);
	}

	@Override
	public void submit(LecternRenderState baseState, RenderState customState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.scale(0.5F, 0.5F, 0.5F);
		customState.stack.submit(poseStack, submitNodeCollector, baseState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
	}

	public static class RenderState {
		public final ItemStackRenderState stack = new ItemStackRenderState();
	}
}
