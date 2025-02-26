package dev.upcraft.sparkweave.api.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Pack {

	<T extends DataProvider> T addProvider(Predicate<DataGenerationContext> enabled, Function<ContextAwarePackOutput, T> factory);

	<T extends DataProvider> T addProvider(Predicate<DataGenerationContext> enabled, Pack.RegistryDependentFactory<T> factory);

	@FunctionalInterface
	interface RegistryDependentFactory<T extends DataProvider> {
		T create(ContextAwarePackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture);
	}
}
