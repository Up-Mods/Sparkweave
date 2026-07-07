//FIXME
//package dev.upcraft.sparkweave.client.event;
//
//import com.google.common.base.Preconditions;
//import com.mojang.datafixers.util.Pair;
//import dev.upcraft.sparkweave.SparkweaveMod;
//import dev.upcraft.sparkweave.api.client.render.CustomArmorRenderer;
//import dev.upcraft.sparkweave.mixin.client.customarmor.ArmorData;
//import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.model.EntityModel;
//import net.minecraft.client.renderer.entity.EntityRendererProvider;
//import net.minecraft.client.renderer.entity.RenderLayerParent;
//import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.util.Util;
//import net.minecraft.util.context.ContextKey;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.ItemLike;
//import org.jetbrains.annotations.ApiStatus;
//
//import java.util.Arrays;
//import java.util.EnumMap;
//import java.util.Map;
//import java.util.Optional;
//import java.util.function.Supplier;
//
//@ApiStatus.Internal
//public class ArmorRendererRegistry {
//
//	public static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.CHEST, EquipmentSlot.HEAD};
//	public static final Map<EquipmentSlot, ContextKey<ArmorData>> ARMOR_CONTEXT_KEYS = Util.make(new EnumMap<>(EquipmentSlot.class), map -> {
//		for (var armorSlot : ARMOR_SLOTS) {
//			map.put(armorSlot, new ContextKey<>(SparkweaveMod.id("custom_armor_data_%s".formatted(armorSlot.getSerializedName()))));
//		}
//	});
//
//	private static final Map<Pair<Class<? extends LivingEntity>, Item>, Optional<CustomArmorRenderer<? extends LivingEntity, ? extends HumanoidRenderState, ?, ? extends EntityModel<?>>>> RENDERERS = new Object2ObjectOpenHashMap<>();
//	private static final Map<Item, CustomArmorRenderer.Factory<? extends LivingEntity, ? extends HumanoidRenderState, ?, ? extends EntityModel<?>>> FACTORIES = new Object2ObjectOpenHashMap<>();
//
//	public static <E extends LivingEntity, S extends HumanoidRenderState, T, M extends EntityModel<S>> void register(CustomArmorRenderer.Factory<E, S, T, M> factory, Supplier<? extends ItemLike>[] items) {
//		Preconditions.checkArgument(items.length > 0, "Custom armor renderer registered, but no items are attached to it");
//
//		for (Supplier<? extends ItemLike> supplier : items) {
//			Preconditions.checkNotNull(supplier, "Armor item is null or doesn't exist");
//			Item item = Preconditions.checkNotNull(supplier.get().asItem(), "Armor item is null or doesn't exist");
//
//			if (FACTORIES.putIfAbsent(item, factory) != null) {
//				throw new IllegalArgumentException("Custom armor renderer already exists for " + BuiltInRegistries.ITEM.getKey(item));
//			}
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	public static <E extends LivingEntity, S extends HumanoidRenderState, T, M extends EntityModel<S>> void register(CustomArmorRenderer.Factory<E, S, T, M> factory, ItemLike[] items) {
//		var suppliers = Arrays.stream(items).map(itemLike -> (Supplier<ItemLike>) () -> itemLike).toArray(Supplier[]::new);
//		register(factory, suppliers);
//	}
//
//	@SuppressWarnings({"unchecked", "rawtypes"})
//	public static <E extends LivingEntity, S extends HumanoidRenderState, T, M extends EntityModel<S>> Optional<CustomArmorRenderer<E, S, T, M>> get(RenderLayerParent<S, M> renderer, E entity, ItemStack stack) {
//		if(stack.isEmpty()) {
//			return Optional.empty();
//		}
//
//		return (Optional<CustomArmorRenderer<E, S, T, M>>) RENDERERS.computeIfAbsent(Pair.of(entity.getClass(), stack.getItem()), key -> {
//			var factory = FACTORIES.get(key.getSecond());
//			if(factory == null) {
//				return Optional.empty();
//			}
//
//			var mc = Minecraft.getInstance();
//			var ctx = new EntityRendererProvider.Context(mc.getEntityRenderDispatcher(), mc.sparkweave$getBlockModelResolver(), mc.sparkweave$getItemModelResolver(), mc.getMapRenderer(), mc.getResourceManager(), mc.getEntityModels(), mc.getEntityRenderDispatcher().m);
//			return Optional.ofNullable(factory.create(entity, ctx, (RenderLayerParent) renderer));
//		});
//	}
//
//	public static boolean hasRenderer(Pair<Class<? extends LivingEntity>, Item> key) {
//		return RENDERERS.containsKey(key);
//	}
//
//	public static void prepare() {
//		FACTORIES.clear();
//		RENDERERS.clear();
//	}
//}
