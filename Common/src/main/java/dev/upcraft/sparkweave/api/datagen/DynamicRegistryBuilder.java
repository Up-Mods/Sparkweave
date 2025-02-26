package dev.upcraft.sparkweave.api.datagen;

import dev.upcraft.sparkweave.api.datagen.provider.DynamicRegistryEntryProvider;

import java.util.function.Supplier;

public interface DynamicRegistryBuilder {

	DynamicRegistryBuilder add(Supplier<DynamicRegistryEntryProvider> factory);
}
