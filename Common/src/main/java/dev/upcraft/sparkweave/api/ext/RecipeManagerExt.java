package dev.upcraft.sparkweave.api.ext;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Collection;

public interface RecipeManagerExt {

	default  <I extends RecipeInput, T extends Recipe<I>> Collection<RecipeHolder<T>> sparkweave$getAllRecipesForType(RecipeType<T> type) {
		throw new AssertionError("Implemented in Mixin");
	}
}
