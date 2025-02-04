package dev.upcraft.sparkweave.mixin.ext.crafting;

import dev.upcraft.sparkweave.api.util.ext.crafting.ShapedRecipeExt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ShapedRecipe.class)
public abstract class ShapedRecipeMixin implements ShapedRecipeExt {

	@Accessor("result")
	@Override
	public abstract ItemStack sparkweave$getResult();
}
