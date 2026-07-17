package dev.upcraft.sparkweave.api.datagen;

import dev.upcraft.sparkweave.api.datagen.provider.common.SparkweaveRecipeProvider;
import dev.upcraft.sparkweave.datagen.SparkweaveRecipeProviderRunner;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.recipes.RecipeOutput;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Pack {

	<T extends DataProvider> T addProvider(Predicate<DataGenerationContext> enabled, Function<ContextAwarePackOutput, T> factory);

	<T extends DataProvider> T addProvider(Predicate<DataGenerationContext> enabled, Pack.RegistryDependentFactory<T> factory);

	default <T extends DataProvider> T addProvider(Function<ContextAwarePackOutput, T> factory) {
		return addProvider(_ -> true, factory);
	}

	default  <T extends DataProvider> T addProvider(RegistryDependentFactory<T> factory) {
		return addProvider(_ -> true, factory);
	}

	default DataProvider addRecipes(BiFunction<HolderLookup.Provider, RecipeOutput, SparkweaveRecipeProvider> factory) {
		return addRecipes(_ -> true, factory);
	}

	default DataProvider addRecipes(Predicate<DataGenerationContext> enabled, BiFunction<HolderLookup.Provider, RecipeOutput, SparkweaveRecipeProvider> factory) {
		return addProvider(enabled, (output, registriesFuture) -> new SparkweaveRecipeProviderRunner(output, registriesFuture, factory));
	}

	@FunctionalInterface
	interface RegistryDependentFactory<T extends DataProvider> {
		T create(ContextAwarePackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture);
	}
}
