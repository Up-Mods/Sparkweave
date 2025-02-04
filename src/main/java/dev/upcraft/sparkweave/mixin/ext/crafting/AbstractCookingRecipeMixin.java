package dev.upcraft.sparkweave.mixin.ext.crafting;

import dev.upcraft.sparkweave.api.util.ext.crafting.AbstractCookingRecipeExt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractCookingRecipe.class)
public abstract class AbstractCookingRecipeMixin implements AbstractCookingRecipeExt {

	@Accessor("result")
	@Override
	public abstract ItemStack sparkweave$getResult();
}
