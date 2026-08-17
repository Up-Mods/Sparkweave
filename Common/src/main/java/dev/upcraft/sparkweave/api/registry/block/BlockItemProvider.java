package dev.upcraft.sparkweave.api.registry.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface BlockItemProvider {

	default Item createItem(ResourceKey<Block> blockId) {
		if (this instanceof Block block) {
			return new BlockItem(block, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, blockId.identifier())).useBlockDescriptionPrefix());
		}

		throw new IllegalStateException("BlockItemProvider implemented on non-block!");
	}
}
