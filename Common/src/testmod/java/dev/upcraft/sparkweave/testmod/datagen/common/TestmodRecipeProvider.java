package dev.upcraft.sparkweave.testmod.datagen.common;

import dev.upcraft.sparkweave.api.datagen.provider.common.SparkweaveRecipeProvider;
import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import dev.upcraft.sparkweave.testmod.init.TestBlocks;
import dev.upcraft.sparkweave.testmod.init.TestItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

public class TestmodRecipeProvider extends SparkweaveRecipeProvider {

	public TestmodRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
		super(registries, output);
	}

	@Override
	public void buildRecipes() {
		shapeless(RecipeCategory.MISC, Items.DIAMOND)
			.requires(Blocks.DIRT)
			.unlockedBy("has_dirt", has(Blocks.DIRT))
			.group(SparkweaveTestmod.id("dirt_to_diamond"))
			.save(output, SparkweaveTestmod.id("dirt_to_diamond"));

		stairBuilder(TestBlocks.TEST_STAIRS.get(), Ingredient.of(TestBlocks.TEST_BLOCK.get()))
			.unlockedBy("has_testblock", has(TestBlocks.TEST_BLOCK.get()))
			.group(TestBlocks.TEST_STAIRS.getId())
			.save(output);

		signBuilder(TestItems.TEST_SIGN.get(), Ingredient.of(TestBlocks.TEST_BLOCK.get()))
			.unlockedBy("has_testblock", has(TestBlocks.TEST_BLOCK.get()))
			.save(output);

		hangingSign(TestItems.TEST_HANGING_SIGN.get(), TestBlocks.TEST_BLOCK.get());
	}
}
