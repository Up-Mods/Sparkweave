package dev.upcraft.sparkweave.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface ViewableRegistryHandler<T> {
	Map<ResourceLocation, RegistrySupplier<? extends T>> values();

	List<RegistrySupplier<? extends T>> getEntriesOrdered();

	Stream<RegistrySupplier<? extends T>> stream();

	ResourceKey<Registry<T>> registry();

	String getNamespace();
}
