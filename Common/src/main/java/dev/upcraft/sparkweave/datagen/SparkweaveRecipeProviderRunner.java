package dev.upcraft.sparkweave.datagen;

import dev.upcraft.sparkweave.api.datagen.ContextAwarePackOutput;
import dev.upcraft.sparkweave.api.datagen.provider.common.SparkweaveRecipeProvider;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class SparkweaveRecipeProviderRunner extends RecipeProvider.Runner {

	private final ModContainer mod;
	private final BiFunction<HolderLookup.Provider, RecipeOutput, SparkweaveRecipeProvider> factory;

	public SparkweaveRecipeProviderRunner(ContextAwarePackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, BiFunction<HolderLookup.Provider, RecipeOutput, SparkweaveRecipeProvider> factory) {
		super(packOutput, registries);
		this.mod = packOutput.getModContainer();
		this.factory = factory;
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
		return factory.apply(provider, recipeOutput);
	}

	@Override
	public String getName() {
		return "%s::Recipes".formatted(mod.metadata().displayName());
	}
}
