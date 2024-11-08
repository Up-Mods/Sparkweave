package dev.upcraft.sparkweave.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.upcraft.sparkweave.event.LecternMenuRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LecternBlockEntity.class)
public abstract class LecternBlockEntityMixin {
	@Shadow public abstract ItemStack getBook();

	@ModifyReturnValue(method = "hasBook", at = @At("RETURN"))
	private boolean allItemsAreBooks(boolean original) {
		return original || LecternMenuRegistry.validItems().contains(getBook().getItem());
	}

	@Inject(method = "createMenu", at = @At("HEAD"), cancellable = true)
	private void applyCustomLecternMenus(int containerId, Inventory inventory, Player player, CallbackInfoReturnable<AbstractContainerMenu> info) {
		LecternMenuRegistry.MenuFactory factory = LecternMenuRegistry.get(getBook());

		System.out.println(factory);
		if(factory != null)
			info.setReturnValue(factory.create(containerId, inventory, player));
	}
}
