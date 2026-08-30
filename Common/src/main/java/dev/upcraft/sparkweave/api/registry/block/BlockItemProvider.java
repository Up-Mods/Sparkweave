package dev.upcraft.sparkweave.api.registry.block;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public interface BlockItemProvider {

	default ResourceKey<Item> createItemId(ResourceKey<Block> blockId) {
		return Items.blockIdToItemId(blockId);
	}

	default Item createItem(Item.Properties properties) {
		if (this instanceof Block block) {
			return new BlockItem(block, properties);
		}

		throw new IllegalStateException("BlockItemProvider implemented on non-block!");
	}
}
