package dev.upcraft.sparkweave.fabric.mixin;

import dev.upcraft.sparkweave.api.event.ItemMenuInteractionEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
	@Inject(
		method = "doClick",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;updateTutorialInventoryAction(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/inventory/ClickAction;)V"),
		locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true
	)
	private void handleItemClickEvent(int slotId, int button, ClickType clickType, Player player, CallbackInfo ci, ClickAction clickAction, Slot slot, ItemStack slotStack, ItemStack cursorStack) {
		if(ItemMenuInteractionEvent.EVENT.invoker().interactWithItemInMenu((AbstractContainerMenu) (Object) this, player, player.level(), clickAction, slot, slotStack, cursorStack)) {
			ci.cancel();
		}
	}
}
