package dev.upcraft.sparkweave.api.client.event;

import dev.upcraft.sparkweave.api.client.render.CustomArmorRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;

public class CustomArmorRendererRegistryEvent {
	private static final HashMap<Item, CustomArmorRenderer> RENDERERS = new HashMap<>();

	public static void register(CustomArmorRenderer renderer, ItemLike... items) {
		Objects.requireNonNull(renderer, "Custom armor renderer is null");

		if(items.length == 0)
			throw new IllegalArgumentException("Custom armor renderer registered, but no items are attached to it");

		for(ItemLike item : items) {
			Objects.requireNonNull(item.asItem(), "Armor item is null or doesn't exist");

			if(RENDERERS.putIfAbsent(item.asItem(), renderer) != null)
				throw new IllegalArgumentException("Custom armor renderer already exists for " + BuiltInRegistries.ITEM.getKey(item.asItem()));
		}
	}

	@Nullable
	public static CustomArmorRenderer get(Item item) {
		return RENDERERS.get(item);
	}
}
