package dev.upcraft.sparkweave.event;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class LecternMenuRegistry {
	private static final Map<Item, MenuFactory> FACTORIES = new Object2ObjectOpenHashMap<>();

	public static void register(MenuFactory factory, Supplier<ItemLike> itemLike) {
		Preconditions.checkNotNull(itemLike, "Item for menu is null or doesn't exist");
		Item item = Preconditions.checkNotNull(itemLike.get().asItem(), "Item for menu is null or doesn't exist");

		if(FACTORIES.putIfAbsent(item, factory) != null) {
			throw new IllegalArgumentException("Custom lectern menu already exists for " + BuiltInRegistries.ITEM.getKey(item));
		}
	}

	public static void register(MenuFactory factory, ItemLike item) {
		register(factory, () -> item);
	}

	public static Set<Item> validItems() {
		return ImmutableSet.copyOf(FACTORIES.keySet());
	}

	@Nullable
	public static MenuFactory get(ItemStack stack) {
		if(stack.isEmpty())
			return null;

		return FACTORIES.get(stack.getItem());
	}

	@FunctionalInterface
	public interface MenuFactory {
		@Nullable AbstractContainerMenu create(int containerId, Inventory inventory, Player player);
	}
}
