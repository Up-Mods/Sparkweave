package dev.upcraft.sparkweave.mixin.ext.crafting;

import dev.upcraft.sparkweave.api.util.ext.crafting.SmithingTransformRecipeExt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SmithingTransformRecipe.class)
public abstract class SmithingTransformRecipeMixin implements SmithingTransformRecipeExt {

	@Accessor("result")
	@Override
	public abstract ItemStack sparkweave$getResult();
}
