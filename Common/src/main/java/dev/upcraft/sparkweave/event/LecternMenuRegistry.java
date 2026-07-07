package dev.upcraft.sparkweave.event;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class LecternMenuRegistry {
	private static final Map<Item, MenuProviderFactory> FACTORIES = new Object2ObjectOpenHashMap<>();

	public static void register(Supplier<? extends ItemLike> itemLike, MenuProviderFactory factory) {
		Preconditions.checkNotNull(itemLike, "Item for menu is null or doesn't exist");
		Item item = Preconditions.checkNotNull(itemLike.get().asItem(), "Item for menu is null or doesn't exist");

		if(FACTORIES.putIfAbsent(item, factory) != null) {
			throw new IllegalArgumentException("Custom lectern menu already exists for " + BuiltInRegistries.ITEM.getKey(item));
		}
	}

	public static Set<Item> validItems() {
		return ImmutableSet.copyOf(FACTORIES.keySet());
	}

	public static Optional<MenuProviderFactory> get(ItemStack stack) {
		if(stack.isEmpty())
			return Optional.empty();

		return Optional.ofNullable(FACTORIES.get(stack.getItem()));
	}

	@FunctionalInterface
	public interface MenuProviderFactory {
		@Nullable MenuProvider create(Level level, BlockPos pos, Player player, LecternBlockEntity blockEntity, ItemStack stack);
	}
}
