package dev.upcraft.sparkweave.api.registry.item;

import dev.upcraft.sparkweave.api.registry.IdAwareRegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ItemRegistryHandler extends IdAwareRegistryHandler<Item, Item.Properties> {

	default <S extends Item, B extends Block> RegistrySupplier<S> registerForBlock(RegistrySupplier<? extends B> block, BiFunction<B, Item.Properties, S> factory, Item.Properties properties) {
		return registerForBlock(block, factory, () -> properties);
	}

	@SuppressWarnings("unchecked")
	default <S extends Item, B extends Block> RegistrySupplier<S> registerForBlock(RegistrySupplier<? extends B> block, BiFunction<B, Item.Properties, S> factory, Supplier<Item.Properties> properties) {
		return register(Items.blockIdToItemId((ResourceKey<Block>) block.getRegistryKey()), props -> factory.apply(block.get(), props), () -> properties.get().useBlockDescriptionPrefix().requiredFeatures(block.get().requiredFeatures()));
	}

	default <S extends Item, B extends Block> RegistrySupplier<S> registerForBlock(String name, Supplier<? extends B> block, BiFunction<B, Item.Properties, S> factory, Item.Properties properties) {
		return registerForBlock(name, block, factory, () -> properties);
	}

	default <S extends Item, B extends Block> RegistrySupplier<S> registerForBlock(String name, Supplier<? extends B> block, BiFunction<B, Item.Properties, S> factory, Supplier<Item.Properties> properties) {
		return register(name, props -> factory.apply(block.get(), props), () -> properties.get().useBlockDescriptionPrefix().requiredFeatures(block.get().requiredFeatures()));
	}
}
