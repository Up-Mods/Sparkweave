package dev.upcraft.sparkweave.client.event;

import com.google.common.base.Preconditions;
import dev.upcraft.sparkweave.api.client.render.LecternItemRenderer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.Map;
import java.util.Optional;

public class LecternItemRendererRegistry {
	private static final Map<Item, Optional<LecternItemRenderer>> RENDERERS = new Object2ObjectOpenHashMap<>();
	private static final Map<Item, LecternItemRenderer.Factory> FACTORIES = new Object2ObjectOpenHashMap<>();

	public static void register(LecternItemRenderer.Factory factory, ItemLike itemLike) {
		Preconditions.checkNotNull(itemLike, "Item is null or doesn't exist");
		Item item = Preconditions.checkNotNull(itemLike.asItem(), "Item is null or doesn't exist");

		if (FACTORIES.putIfAbsent(item, factory) != null) {
			throw new IllegalArgumentException("Custom lectern item renderer already exists for " + BuiltInRegistries.ITEM.getKey(item.asItem()));
		}
	}

	public static Optional<LecternItemRenderer> get(Item item) {
		return RENDERERS.computeIfAbsent(item, key -> {
			var factory = FACTORIES.get(key);
			if(factory == null) {
				return Optional.empty();
			}

			var mc = Minecraft.getInstance();
			var ctx = new BlockEntityRendererProvider.Context(mc.getBlockEntityRenderDispatcher(), mc.getBlockRenderer(), mc.getItemRenderer(), mc.getEntityRenderDispatcher(), mc.getEntityModels(), mc.font);
			return Optional.ofNullable(factory.create(ctx));
		});
	}
}
