package dev.upcraft.sparkweave.api.registry.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface BlockItemProvider {

	default Item createItem() {
		if (this instanceof Block block) {
			return new BlockItem(block, new Item.Properties());
		}

		throw new IllegalStateException("BlockItemProvider implemented on non-block!");
	}
}
