package dev.upcraft.sparkweave.api.registry.item;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTab;

public interface CreativeTabFiller {

	void addItemsToTab(CreativeModeTab.ItemDisplayParameters displayParameters, CreativeModeTab.Output collector, RandomSource random);
}
