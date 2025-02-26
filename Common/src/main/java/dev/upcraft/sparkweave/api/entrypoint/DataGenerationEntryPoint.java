package dev.upcraft.sparkweave.api.entrypoint;

import dev.upcraft.sparkweave.api.datagen.DataGenerationContext;
import dev.upcraft.sparkweave.api.datagen.DynamicRegistryBuilder;

public interface DataGenerationEntryPoint {

	default void generateDynamicRegistryEntries(DynamicRegistryBuilder builder) {

	}

	void generate(DataGenerationContext ctx);
}
