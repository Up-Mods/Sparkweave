package dev.upcraft.sparkweave.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.upcraft.sparkweave.api.ext.LecternBlockEntityExt;
import dev.upcraft.sparkweave.event.LecternMenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Clearable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LecternBlockEntity.class)
public abstract class LecternBlockEntityMixin extends BlockEntity implements Clearable, MenuProvider, LecternBlockEntityExt {
	@Shadow public abstract ItemStack getBook();

	public LecternBlockEntityMixin(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) { super($$0, $$1, $$2); }

	@ModifyReturnValue(method = "hasBook", at = @At("RETURN"))
	private boolean allowCustomBooks(boolean original) {
		return original || LecternMenuRegistry.validItems().contains(getBook().getItem());
	}

	@Override
	public ItemOwner sparkweave$asItemOwner() {
		return new ItemOwner() {
			@Override
			public Level level() {
				return getLevel();
			}

			@Override
			public Vec3 position() {
				return Vec3.atLowerCornerOf(getBlockPos()).add(0.5D, 1.0625D, 0.5D);
			}

			@Override
			public float getVisualRotationYInDegrees() {
				return getBlockState().getValue(LecternBlock.FACING).toYRot();
			}
		};
	}
}
