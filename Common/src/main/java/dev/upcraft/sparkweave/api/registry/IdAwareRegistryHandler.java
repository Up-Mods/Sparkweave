package dev.upcraft.sparkweave.api.registry;

import net.minecraft.resources.ResourceKey;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IdAwareRegistryHandler<T, P> extends RegistryHandler<T> {

	default <S extends T> RegistrySupplier<S> register(String name, Function<P, S> factory, P properties) {
		return register(name, factory, () -> properties);
	}

	default <S extends T> RegistrySupplier<S> register(ResourceKey<T> id, Function<P, S> factory, P properties) {
		return register(id, factory, () -> properties);
	}

	<S extends T> RegistrySupplier<S> register(String name, Function<P, S> factory, Supplier<P> properties);

	<S extends T> RegistrySupplier<S> register(ResourceKey<T> id, Function<P, S> factory, Supplier<P> properties);
}
