package dev.upcraft.sparkweave.api.registry;

import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.api.registry.block.BlockEntityRegistryHandler;
import dev.upcraft.sparkweave.api.registry.block.BlockRegistryHandler;
import dev.upcraft.sparkweave.api.registry.datacomponent.DataComponentRegistryHandler;
import dev.upcraft.sparkweave.api.registry.entity.EntityRegistryHandler;
import dev.upcraft.sparkweave.api.registry.item.ItemRegistryHandler;
import dev.upcraft.sparkweave.registry.IdAwareRegistryHandlerImpl;
import dev.upcraft.sparkweave.registry.block.BlockEntityRegistryHandlerImpl;
import dev.upcraft.sparkweave.registry.block.BlockRegistryHandlerImpl;
import dev.upcraft.sparkweave.registry.datacomponents.DataComponentRegistryHandlerImpl;
import dev.upcraft.sparkweave.registry.entity.EntityRegistryHandlerImpl;
import dev.upcraft.sparkweave.registry.item.ItemRegistryHandlerImpl;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface RegistryHandler<T> extends Consumer<RegistryService>, RegistryCreator<T>, ViewableRegistryHandler<T> {

	static <T> RegistryHandler<T> create(ResourceKey<Registry<T>> registryKey, String namespace) {
		return RegistryService.get().createRegistryHandler(registryKey, namespace);
	}

	static <T, P> IdAwareRegistryHandler<T, P> create(ResourceKey<Registry<T>> registryKey, String namespace, BiFunction<P, ResourceKey<T>, P> idMapper) {
		var handler = create(registryKey, namespace);
		return new IdAwareRegistryHandlerImpl<>(handler, idMapper);
	}

	static BlockEntityRegistryHandler blockEntities(String namespace) {
		var handler = create(Registries.BLOCK_ENTITY_TYPE, namespace);
		return new BlockEntityRegistryHandlerImpl(handler);
	}

	static BlockRegistryHandler blocks(String namespace) {
		var handler = create(Registries.BLOCK, namespace);
		return new BlockRegistryHandlerImpl(handler);
	}

	static DataComponentRegistryHandler dataComponents(String namespace) {
		var handler = create(Registries.DATA_COMPONENT_TYPE, namespace);
		return new DataComponentRegistryHandlerImpl(handler);
	}

	static EntityRegistryHandler entities(String namespace) {
		var handler = create(Registries.ENTITY_TYPE, namespace);
		return new EntityRegistryHandlerImpl(handler);
	}

	static ItemRegistryHandler items(String namespace) {
		var handler = create(Registries.ITEM, namespace);
		return new ItemRegistryHandlerImpl(handler);
	}

	<S extends T> RegistrySupplier<S> register(String name, Supplier<S> factory);

	<S extends T> RegistrySupplier<S> register(ResourceKey<T> id, Supplier<S> factory);

	@Override
	default void accept(RegistryService registryService) {
		registryService.handleRegister(this);
	}
}
