package dev.upcraft.sparkweave.api.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class LecternItemRenderer {
	protected final BlockEntityRendererProvider.Context context;

	public LecternItemRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
	}

	public BlockEntityRendererProvider.Context blockEntityContext() {
		return context;
	}

	public abstract void renderBook(LecternBlockEntity lecternBlockEntity, BlockState blockState, ItemStack itemStack, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay);

	@Nullable
	public abstract Model getBookModel();

	@FunctionalInterface
	public interface Factory {
		@Nullable LecternItemRenderer create(BlockEntityRendererProvider.Context context);
	}
}
