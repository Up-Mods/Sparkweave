package dev.upcraft.sparkweave.registry.entity;

import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import dev.upcraft.sparkweave.api.registry.entity.EntityRegistryHandler;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class EntityRegistryHandlerImpl implements EntityRegistryHandler {

	private final RegistryHandler<EntityType<?>> delegate;

	public EntityRegistryHandlerImpl(RegistryHandler<EntityType<?>> delegate) {
		this.delegate = delegate;
	}

	@Override
	public <S extends EntityType<?>> RegistrySupplier<S> register(String name, Supplier<S> factory) {
		return delegate.register(name, factory);
	}

	@Override
	public <S extends EntityType<?>> RegistrySupplier<S> register(ResourceKey<EntityType<?>> id, Supplier<S> factory) {
		return delegate.register(id, factory);
	}

	@Override
	public Registry<EntityType<?>> createNewRegistry(boolean sync, @Nullable Identifier defaultEntry) {
		return delegate.createNewRegistry(sync, defaultEntry);
	}

	@Override
	public Map<Identifier, RegistrySupplier<? extends EntityType<?>>> values() {
		return delegate.values();
	}

	@Override
	public List<RegistrySupplier<? extends EntityType<?>>> getEntriesOrdered() {
		return delegate.getEntriesOrdered();
	}

	@Override
	public Stream<RegistrySupplier<? extends EntityType<?>>> stream() {
		return delegate.stream();
	}

	@Override
	public ResourceKey<Registry<EntityType<?>>> registry() {
		return delegate.registry();
	}

	@Override
	public String getNamespace() {
		return delegate.getNamespace();
	}

	@Override
	public void accept(RegistryService registryService) {
		delegate.accept(registryService);
	}
}
