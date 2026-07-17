package dev.upcraft.sparkweave.api.datagen.provider.common;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

public abstract class SparkweaveRecipeProvider extends RecipeProvider {

	public SparkweaveRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
		super(registries, output);
	}

	// making it public because fabric AWs it
	@Override
	public abstract void buildRecipes();
}
