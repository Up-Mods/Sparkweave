package dev.upcraft.sparkweave.client.event;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;
import dev.upcraft.sparkweave.api.client.render.CustomArmorRenderer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@ApiStatus.Internal
public class ArmorRendererRegistry {

	private static final Map<Pair<Class<? extends LivingEntity>, Item>, Optional<CustomArmorRenderer<? extends LivingEntity, ? extends EntityModel<?>>>> RENDERERS = new Object2ObjectOpenHashMap<>();
	private static final Map<Item, CustomArmorRenderer.Factory<? extends LivingEntity, ? extends EntityModel<?>>> FACTORIES = new Object2ObjectOpenHashMap<>();

	public static <E extends LivingEntity, M extends EntityModel<E>> void register(CustomArmorRenderer.Factory<E, M> factory, Supplier<ItemLike>[] items) {
		Preconditions.checkArgument(items.length > 0, "Custom armor renderer registered, but no items are attached to it");

		for (Supplier<ItemLike> supplier : items) {
			Preconditions.checkNotNull(supplier, "Armor item is null or doesn't exist");
			Item item = Preconditions.checkNotNull(supplier.get().asItem(), "Armor item is null or doesn't exist");

			if (FACTORIES.putIfAbsent(item, factory) != null) {
				throw new IllegalArgumentException("Custom armor renderer already exists for " + BuiltInRegistries.ITEM.getKey(item));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <E extends LivingEntity, M extends EntityModel<E>> void register(CustomArmorRenderer.Factory<E, M> factory, ItemLike[] items) {
		var suppliers = Arrays.stream(items).map(it -> (Supplier<ItemLike>) () -> it).toArray(Supplier[]::new);
		register(factory, suppliers);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <E extends LivingEntity, M extends EntityModel<E>> Optional<CustomArmorRenderer<E, M>> get(RenderLayerParent<E, M> renderer, E entity, ItemStack stack) {
		if(stack.isEmpty()) {
			return Optional.empty();
		}

		return (Optional<CustomArmorRenderer<E,M>>) RENDERERS.computeIfAbsent(Pair.of(entity.getClass(), stack.getItem()), key -> {
			var factory = FACTORIES.get(key.getSecond());
			if(factory == null) {
				return Optional.empty();
			}

			var mc = Minecraft.getInstance();
			var ctx = new EntityRendererProvider.Context(mc.getEntityRenderDispatcher(), mc.getItemRenderer(), mc.getBlockRenderer(), mc.getEntityRenderDispatcher().getItemInHandRenderer(), mc.getResourceManager(), mc.getEntityModels(), mc.font);
			return Optional.ofNullable(factory.create(entity, ctx, (RenderLayerParent) renderer));
		});
	}
}
