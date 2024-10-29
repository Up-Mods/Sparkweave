package dev.upcraft.sparkweave.api.registry;

import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.registry.IdAwareRegistryHandlerImpl;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface RegistryHandler<T> extends Consumer<RegistryService>, ViewableRegistryHandler<T>, RegistryCreator<T> {

	static <T> RegistryHandler<T> create(ResourceKey<Registry<T>> registryKey, String namespace) {
		return RegistryService.get().createRegistryHandler(registryKey, namespace);
	}

	static <T, P> IdAwareRegistryHandler<T, P> create(ResourceKey<Registry<T>> registryKey, String namespace, BiFunction<P, ResourceKey<T>, P> idMapper) {
		var handler = create(registryKey, namespace);
		return new IdAwareRegistryHandlerImpl<>(handler, idMapper);
	}

	<S extends T> RegistrySupplier<S> register(String name, Supplier<S> factory);

	<S extends T> RegistrySupplier<S> register(ResourceKey<T> id, Supplier<S> factory);

	@Override
	default void accept(RegistryService registryService) {
		registryService.handleRegister(this);
	}
}
