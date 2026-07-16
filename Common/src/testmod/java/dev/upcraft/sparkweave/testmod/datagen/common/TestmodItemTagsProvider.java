package dev.upcraft.sparkweave.testmod.datagen.common;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.datagen.provider.SparkweaveItemTagProvider;
import dev.upcraft.sparkweave.testmod.data.TestmodTags;
import dev.upcraft.sparkweave.testmod.init.TestItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class TestmodItemTagsProvider extends SparkweaveItemTagProvider {

	public TestmodItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, SparkweaveMod.MODID, lookupProvider);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		existingTag(ItemTags.LECTERN_BOOKS)
			.add(TestItems.TEST_ITEM);

		tag(TestmodTags.Items.MAGE_ROBES_REPAIR_MATERIALS, "Mage Robes repair materials")
			.add(Items.LEATHER);
	}
}
