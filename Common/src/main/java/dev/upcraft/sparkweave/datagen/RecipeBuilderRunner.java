package dev.upcraft.sparkweave.datagen;

import dev.upcraft.sparkweave.api.datagen.provider.SparkweaveRecipeProvider;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class RecipeBuilderRunner extends RecipeProvider.Runner {

	private final ModContainer mod;
	private final BiFunction<HolderLookup.Provider, RecipeOutput, SparkweaveRecipeProvider> factory;

	public RecipeBuilderRunner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, ModContainer mod, BiFunction<HolderLookup.Provider, RecipeOutput, SparkweaveRecipeProvider> factory) {
		super(packOutput, registries);
		this.mod = mod;
		this.factory = factory;
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
		return factory.apply(provider, recipeOutput);
	}

	@Override
	public String getName() {
		return "%s Recipes".formatted(mod.metadata().displayName());
	}
}
