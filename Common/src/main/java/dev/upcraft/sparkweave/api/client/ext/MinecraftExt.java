package dev.upcraft.sparkweave.api.client.ext;

import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.item.ItemModelResolver;

public interface MinecraftExt {

	default BlockModelResolver sparkweave$getBlockModelResolver() {
		throw new AssertionError("Implemented in Mixin");
	}

	default ItemModelResolver sparkweave$getItemModelResolver() {
		throw new AssertionError("Implemented in Mixin");
	}
}
