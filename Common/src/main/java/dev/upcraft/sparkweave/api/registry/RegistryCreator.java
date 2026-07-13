package dev.upcraft.sparkweave.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public interface RegistryCreator<T> {
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
}
