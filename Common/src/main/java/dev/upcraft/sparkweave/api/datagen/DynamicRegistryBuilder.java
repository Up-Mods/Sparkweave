package dev.upcraft.sparkweave.api.datagen;

import dev.upcraft.sparkweave.api.datagen.provider.common.dynamic.SparkweaveDynamicRegistryEntryProvider;

import java.util.function.Supplier;

public interface DynamicRegistryBuilder {

	DynamicRegistryBuilder add(Supplier<SparkweaveDynamicRegistryEntryProvider> factory);
}
