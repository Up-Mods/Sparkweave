package dev.upcraft.sparkweave.registry.datacomponents;

import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import dev.upcraft.sparkweave.api.registry.datacomponent.DataComponentRegistryHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class DataComponentRegistryHandlerImpl implements DataComponentRegistryHandler {

	private final RegistryHandler<DataComponentType<?>> delegate;

	public DataComponentRegistryHandlerImpl(RegistryHandler<DataComponentType<?>> delegate) {
		this.delegate = delegate;
	}

	@Override
	public <S extends DataComponentType<?>> RegistrySupplier<S> register(String name, Supplier<S> factory) {
		return delegate.register(name, factory);
	}

	@Override
	public <S extends DataComponentType<?>> RegistrySupplier<S> register(ResourceKey<DataComponentType<?>> id, Supplier<S> factory) {
		return delegate.register(id, factory);
	}

	@Override
	public Registry<DataComponentType<?>> createNewRegistry(boolean sync, @Nullable Identifier defaultEntry) {
		return delegate.createNewRegistry(sync, defaultEntry);
	}

	@Override
	public Map<Identifier, RegistrySupplier<? extends DataComponentType<?>>> values() {
		return delegate.values();
	}

	@Override
	public List<RegistrySupplier<? extends DataComponentType<?>>> getEntriesOrdered() {
		return delegate.getEntriesOrdered();
	}

	@Override
	public Stream<RegistrySupplier<? extends DataComponentType<?>>> stream() {
		return delegate.stream();
	}

	@Override
	public ResourceKey<Registry<DataComponentType<?>>> registry() {
		return delegate.registry();
	}

	@Override
	public String getNamespace() {
		return delegate.getNamespace();
	}

	@Override
	public <T> RegistrySupplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> factory) {
		return register(name, () -> factory.apply(new DataComponentType.Builder<>()).build());
	}

	@Override
	public void accept(RegistryService registryService) {
		delegate.accept(registryService);
	}
}
