package dev.upcraft.sparkweave.api.registry;

import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface RegistryHandler<T> extends Consumer<RegistryService> {

	static <T> RegistryHandler<T> create(ResourceKey<Registry<T>> registryKey, String namespace) {
		return RegistryService.get().createRegistryHandler(registryKey, namespace);
	}

	<S extends T> RegistrySupplier<S> register(String name, Supplier<S> factory);

	<S extends T> RegistrySupplier<S> register(ResourceKey<T> id, Supplier<S> factory);

	Map<Identifier, RegistrySupplier<? extends T>> values();

	List<RegistrySupplier<? extends T>> getEntriesOrdered();

	Stream<RegistrySupplier<? extends T>> stream();

	ResourceKey<Registry<T>> registry();

	/**
	 * @param sync         Whether the registry int IDs should be synchronized to each client
	 * @param defaultEntry The default entry for the registry. if {@code null} this method will return a {@link net.minecraft.core.MappedRegistry}, otherwise a {@link net.minecraft.core.DefaultedRegistry}
	 */
	Registry<T> createNewRegistry(boolean sync, @Nullable Identifier defaultEntry);

	default Registry<T> createNewRegistry(boolean sync) {
		return createNewRegistry(sync, null);
	}

	default Registry<T> createNewRegistry() {
		return createNewRegistry(true, null);
	}

	@Override
	default void accept(RegistryService registryService) {
		registryService.handleRegister(this);
	}
}
