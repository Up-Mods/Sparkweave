package dev.upcraft.sparkweave.client.event;

import com.google.common.base.Preconditions;
import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.client.render.LecternItemRenderer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public class LecternItemRendererRegistryImpl {

	public static final ContextKey<LecternItemRenderer<?>> ITEM_RENDERER_KEY = new ContextKey<>(SparkweaveMod.id("lectern_item_renderer"));
	public static final ContextKey<Object> ITEM_RENDERER_DATA_KEY = new ContextKey<>(SparkweaveMod.id("lectern_item_renderer_data"));

	private static final Map<Item, LecternItemRenderer<?>> RENDERERS = new Object2ObjectOpenHashMap<>();
	private static final Map<Item, LecternItemRenderer.Factory> FACTORIES = new Object2ObjectOpenHashMap<>();

	public static void register(LecternItemRenderer.Factory factory, Supplier<? extends ItemLike> itemLike) {
		Preconditions.checkNotNull(itemLike, "Item is null or doesn't exist");
		Item item = Preconditions.checkNotNull(itemLike.get().asItem(), "Item is null or doesn't exist");

		if (FACTORIES.putIfAbsent(item, factory) != null) {
			throw new IllegalArgumentException("Custom lectern item renderer already exists for " + BuiltInRegistries.ITEM.getKey(item));
		}
	}

	public static void onResourceManagerReload(BlockEntityRendererProvider.Context context) {
		RENDERERS.clear();
		FACTORIES.forEach((item, factory) -> {
			var renderer = factory.create(context);
			if(renderer != null) {
				RENDERERS.put(item, renderer);
			}
		});
	}

	@Nullable
	public static LecternItemRenderer<?> get(ItemStack stack) {
		if(stack.isEmpty()) {
			return null;
		}

		return RENDERERS.get(stack.getItem());
	}
}
