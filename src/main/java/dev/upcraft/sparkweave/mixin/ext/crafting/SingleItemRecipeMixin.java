package dev.upcraft.sparkweave.mixin.ext.crafting;

import dev.upcraft.sparkweave.api.util.ext.crafting.SingleItemRecipeExt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SingleItemRecipe.class)
public abstract class SingleItemRecipeMixin implements SingleItemRecipeExt {

	@Accessor("result")
	@Override
	public abstract ItemStack sparkweave$getResult();
}
