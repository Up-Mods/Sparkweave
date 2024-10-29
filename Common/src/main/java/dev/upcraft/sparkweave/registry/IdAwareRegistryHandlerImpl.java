package dev.upcraft.sparkweave.registry;

import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.api.registry.IdAwareRegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class IdAwareRegistryHandlerImpl<T, P> implements IdAwareRegistryHandler<T, P> {

	private final RegistryHandler<T> delegate;
	private final BiFunction<P, ResourceKey<T>, P> idMapper;

	public IdAwareRegistryHandlerImpl(RegistryHandler<T> delegate, BiFunction<P, ResourceKey<T>, P> idMapper) {
		this.delegate = delegate;
		this.idMapper = idMapper;
	}

	@Override
	public <S extends T> RegistrySupplier<S> register(String name, Function<P, S> factory, Supplier<P> properties) {
		ResourceKey<T> id = ResourceKey.create(registry(), ResourceLocation.fromNamespaceAndPath(getNamespace(), name));
		return register(id, factory, properties);
	}

	@Override
	public <S extends T> RegistrySupplier<S> register(ResourceKey<T> id, Function<P, S> factory, Supplier<P> properties) {
		return delegate.register(id, () -> factory.apply(idMapper.apply(properties.get(), id)));
	}

	@Override
	public void accept(RegistryService registryService) {
		delegate.accept(registryService);
	}

	@Override
	public Registry<T> createNewRegistry(boolean sync, @Nullable ResourceLocation defaultEntry) {
		return delegate.createNewRegistry(sync, defaultEntry);
	}

	@Override
	public Map<ResourceLocation, RegistrySupplier<? extends T>> values() {
		return delegate.values();
	}

	@Override
	public List<RegistrySupplier<? extends T>> getEntriesOrdered() {
		return delegate.getEntriesOrdered();
	}

	@Override
	public Stream<RegistrySupplier<? extends T>> stream() {
		return delegate.stream();
	}

	@Override
	public ResourceKey<Registry<T>> registry() {
		return delegate.registry();
	}

	@Override
	public String getNamespace() {
		return delegate.getNamespace();
	}
}
