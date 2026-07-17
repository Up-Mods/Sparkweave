package dev.upcraft.sparkweave.mixin.datagen;

import dev.upcraft.sparkweave.api.ext.datagen.RecipeBuilderExt;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RecipeBuilder.class)
public interface RecipeBuilderMixin extends RecipeBuilderExt {

	@Shadow
	RecipeBuilder group(@Nullable String s);

	@Shadow
	void save(RecipeOutput output, String id);

	@SuppressWarnings("unchecked")
	@Override
	default <T extends RecipeBuilder> T group(@Nullable Identifier group) {
		return (T) this.group(group.toShortLanguageKey());
	}

	@Override
	default void save(RecipeOutput output, Identifier id) {
		this.save(output, id.toString());
	}
}
