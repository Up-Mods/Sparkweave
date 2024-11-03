package dev.upcraft.sparkweave.api.client.render;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.Optional;

public class ArmorRendererRegistry {

	private static final HashMap<Pair<Class<? extends LivingEntity>, Item>, Optional<CustomArmorRenderer<? extends LivingEntity, ? extends EntityModel<?>>>> RENDERERS = new HashMap<>();
	private static final HashMap<Item, CustomArmorRenderer.Factory<? extends LivingEntity, ? extends EntityModel<?>>> FACTORIES = new HashMap<>();

	public static <E extends LivingEntity, M extends EntityModel<E>> void register(CustomArmorRenderer.Factory<E, M> factory, ItemLike... items) {
		Preconditions.checkArgument(items.length > 0, "Custom armor renderer registered, but no items are attached to it");

		for (ItemLike itemLike : items) {
			Preconditions.checkNotNull(itemLike, "Armor item is null or doesn't exist");
			Item item = Preconditions.checkNotNull(itemLike.asItem(), "Armor item is null or doesn't exist");

			if (FACTORIES.putIfAbsent(item, factory) != null) {
				throw new IllegalArgumentException("Custom armor renderer already exists for " + BuiltInRegistries.ITEM.getKey(item.asItem()));
			}
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <E extends LivingEntity, M extends EntityModel<E>> Optional<CustomArmorRenderer<E, M>> get(RenderLayerParent<E, M> renderer, E entity, Item item) {
		return (Optional<CustomArmorRenderer<E,M>>) RENDERERS.computeIfAbsent(Pair.of(entity.getClass(), item), key -> {
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
