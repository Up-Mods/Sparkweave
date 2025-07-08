package dev.upcraft.sparkweave.util;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.item.CreativeTabHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LightBlock;

public class SparkweaveDevCreativeTab {

	public static final ResourceKey<CreativeModeTab> DEVELOPER_MODE_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, SparkweaveMod.id("developer_tab"));

	public static CreativeModeTab buildTab() {
		return CreativeTabHelper.newBuilder(DEVELOPER_MODE_TAB).icon(Items.COMMAND_BLOCK::getDefaultInstance).displayItems((itemDisplayParameters, output) -> {
			output.accept(Items.COMMAND_BLOCK);
			output.accept(Items.CHAIN_COMMAND_BLOCK);
			output.accept(Items.REPEATING_COMMAND_BLOCK);
			output.accept(Items.STRUCTURE_BLOCK);
			output.accept(Items.STRUCTURE_VOID);
			for(int light : new int[]{ 15, 7, 3, 1 }) {
				output.accept(LightBlock.setLightOnStack(new ItemStack(Items.LIGHT), light));
			}

			output.accept(Items.DEBUG_STICK);

		}).build();
	}
}
