package dev.upcraft.sparkweave.registry.item;

import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.item.ItemRegistryHandler;
import dev.upcraft.sparkweave.registry.IdAwareRegistryHandlerImpl;
import net.minecraft.world.item.Item;

public class ItemRegistryHandlerImpl extends IdAwareRegistryHandlerImpl<Item, Item.Properties> implements ItemRegistryHandler {

	public ItemRegistryHandlerImpl(RegistryHandler<Item> delegate) {
		super(delegate, Item.Properties::setId);
	}
}
