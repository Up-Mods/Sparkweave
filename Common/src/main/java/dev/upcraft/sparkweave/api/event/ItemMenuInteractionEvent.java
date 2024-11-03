package dev.upcraft.sparkweave.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ItemMenuInteractionEvent {

	Event<ItemMenuInteractionEvent> EVENT = Event.create(ItemMenuInteractionEvent.class, listeners -> (menu, player, level, clickAction, slot, slotStack, cursorStack) -> {
		for (ItemMenuInteractionEvent listener : listeners) {
			if (listener.interactWithItemInMenu(menu, player, level, clickAction, slot, slotStack, cursorStack)) {
				return true;
			}
		}

		return false;
	});

	/**
	 * @param clickAction The mouse button being clicked
	 * @param slot        The slot the item being interacted with is in
	 * @param slotStack   The {@link ItemStack} in the slot that's being interacted with
	 * @param cursorStack The {@link ItemStack} currently being held by the user's cursor
	 * @return {@code true} to indicate that the event has been handled and further processing should be canceled, {@code false} to fall back to default processing of the click.
	 */
	boolean interactWithItemInMenu(AbstractContainerMenu menu, Player player, Level level, ClickAction clickAction, Slot slot, ItemStack slotStack, ItemStack cursorStack);
}
