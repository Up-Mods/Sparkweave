package dev.upcraft.sparkweave.api.ext.datagen;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.List;

public interface RegistrySetBuilderExt {

	default List<? extends ResourceKey<? extends Registry<?>>> getEntryKeys() {
		throw new AssertionError("Implemented in Mixin");
	}
}
