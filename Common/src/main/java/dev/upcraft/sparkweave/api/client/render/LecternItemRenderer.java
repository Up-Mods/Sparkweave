package dev.upcraft.sparkweave.api.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.LecternRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public abstract class LecternItemRenderer<T> {

	private final Class<T> renderStateType;

	protected LecternItemRenderer(Class<T> renderStateType) {
		this.renderStateType = renderStateType;
	}

	public abstract T createRenderState();

	public abstract void extractRenderState(LecternBlockEntity blockEntity, LecternRenderState baseState, T customState, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress);

	public abstract void submit(LecternRenderState baseState, T customState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera);

	public Class<T> getRenderStateType() {
		return this.renderStateType;
	}

	@FunctionalInterface
	public interface Factory {
		@Nullable LecternItemRenderer<?> create(BlockEntityRendererProvider.Context context);
	}
}
