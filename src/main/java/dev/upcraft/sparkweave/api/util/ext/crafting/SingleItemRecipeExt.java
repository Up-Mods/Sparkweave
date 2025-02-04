package dev.upcraft.sparkweave.api.util.ext.crafting;

import dev.upcraft.sparkweave.api.util.crafting.RecipeWithResult;
import net.minecraft.world.item.ItemStack;

public interface SingleItemRecipeExt extends RecipeWithResult {

	@Override
	default ItemStack sparkweave$getResult() {
		throw new UnsupportedOperationException();
	}
}
