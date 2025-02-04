package dev.upcraft.sparkweave.mixin.ext.crafting;

import dev.upcraft.sparkweave.api.util.ext.crafting.ShapelessRecipeExt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ShapelessRecipe.class)
public abstract class ShapelessRecipeMixin implements ShapelessRecipeExt {

	@Accessor("result")
	@Override
	public abstract ItemStack sparkweave$getResult();
}
