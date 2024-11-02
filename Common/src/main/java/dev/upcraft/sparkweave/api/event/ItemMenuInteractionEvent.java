package dev.upcraft.sparkweave.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ItemMenuInteractionEvent {
	Event<ItemMenuInteractionEvent> EVENT = Event.create(ItemMenuInteractionEvent.class, listeners -> (menu, player, level, clickAction, slot, slotStack, cursorStack) -> {
		for(ItemMenuInteractionEvent listener : listeners) {
			boolean cancel = listener.interactWithItemInMenu(menu, player, level, clickAction, slot, slotStack, cursorStack);

			if(cancel)
				return true;
		}

		return false;
	});

	/**
	 * @param menu The menu the player is in
	 * @param player The player doing the interaction
	 * @param clickAction The mouse button being clicked
	 * @param slot The slot the item being interacted with is in
	 * @param slotStack The {@link ItemStack} in the menu that's being interacted with
	 * @param cursorStack The {@link ItemStack} currently being held by the user's cursor
	 * @return Whether to cancel the normal interaction that would happen
	 */
	boolean interactWithItemInMenu(AbstractContainerMenu menu, Player player, Level level, ClickAction clickAction, Slot slot, ItemStack slotStack, ItemStack cursorStack);
}
