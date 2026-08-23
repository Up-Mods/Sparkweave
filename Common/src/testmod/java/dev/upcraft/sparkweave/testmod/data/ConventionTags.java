package dev.upcraft.sparkweave.testmod.data;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ConventionTags {
	public static class Items {
		public static final TagKey<Item> FERTILIZERS = cTag(Registries.ITEM, "fertilizers");
	}

	private static <T> TagKey<T> cTag(ResourceKey<Registry<T>> registry, String name) {
		return TagKey.create(registry, Identifier.fromNamespaceAndPath("c", name));
	}
}
