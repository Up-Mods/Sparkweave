package dev.upcraft.sparkweave.api.ext.datagen;

import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public interface RecipeBuilderExt {

	default <T extends RecipeBuilder> T group(@Nullable Identifier group) {
		throw new AssertionError("Implemented in Mixin");
	}

	default void save(RecipeOutput output, Identifier id) {
		throw new AssertionError("Implemented in Mixin");
	}
}
