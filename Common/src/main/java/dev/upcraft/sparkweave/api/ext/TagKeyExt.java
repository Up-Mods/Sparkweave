package dev.upcraft.sparkweave.api.ext;

import net.minecraft.network.chat.Component;

public interface TagKeyExt {

	default String getDescriptionId() {
		throw new AssertionError("Implemented in Mixin");
	}

	default Component getName() {
		throw new AssertionError("Implemented in Mixin");
	}
}
