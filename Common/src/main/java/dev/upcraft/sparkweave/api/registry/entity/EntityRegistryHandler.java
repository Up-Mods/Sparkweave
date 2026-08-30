package dev.upcraft.sparkweave.api.registry.entity;

import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.UnaryOperator;

public interface EntityRegistryHandler extends RegistryHandler<EntityType<?>> {

	default <E extends Entity> RegistrySupplier<EntityType<E>> register(String name, EntityType.EntityFactory<E> factory, MobCategory category) {
		return register(name, factory, category, UnaryOperator.identity());
	}

	default <E extends Entity> RegistrySupplier<EntityType<E>> register(String name, EntityType.EntityFactory<E> factory, MobCategory category, UnaryOperator<EntityType.Builder<E>> properties) {
		var id = ResourceKey.create(registry(), Identifier.fromNamespaceAndPath(getNamespace(), name));
		return register(id, factory, category, properties);
	}

	default <E extends Entity> RegistrySupplier<EntityType<E>> register(ResourceKey<EntityType<?>> id, EntityType.EntityFactory<E> factory, MobCategory category) {
		return register(id, factory, category, UnaryOperator.identity());
	}

	default <E extends Entity> RegistrySupplier<EntityType<E>> register(ResourceKey<EntityType<?>> id, EntityType.EntityFactory<E> factory, MobCategory category, UnaryOperator<EntityType.Builder<E>> properties) {
		return register(id, () -> properties.apply(EntityType.Builder.of(factory, category)).build(id));
	}
}
