package dev.upcraft.sparkweave.api.registry.block;

import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public interface BlockItemProvider {

    default Item createItem(BlockItemProvider.RegistryHelper registry) {
		return registry.accept(Item::new, new Item.Properties());
    }

	@FunctionalInterface
	interface RegistryHelper {

		default <T extends Item> T accept(Function<Item.Properties, T> factory, Item.Properties properties) {
			return accept(factory, () -> properties);
		}

		<T extends Item> T accept(Function<Item.Properties, T> factory, Supplier<Item.Properties> properties);

	}
}
