package dev.upcraft.sparkweave.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.upcraft.sparkweave.api.event.ItemMenuInteractionEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {

	@Inject(method = "doClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;updateTutorialInventoryAction(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/inventory/ClickAction;)V"), cancellable = true)
	private void handleItemClickEvent(int slotIndex, int buttonNum, ContainerInput containerInput, Player player, CallbackInfo ci, @Local ClickAction clickAction, @Local Slot slot, @Local(name = "clicked") ItemStack clicked, @Local(name = "carried") ItemStack carried) {
		if (ItemMenuInteractionEvent.EVENT.invoker().interactWithItemInMenu((AbstractContainerMenu) (Object) this, player, player.level(), clickAction, slot, clicked, carried)) {
			ci.cancel();
		}
	}
}
