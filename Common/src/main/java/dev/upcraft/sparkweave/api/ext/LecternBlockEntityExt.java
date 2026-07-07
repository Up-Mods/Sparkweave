package dev.upcraft.sparkweave.api.ext;

import net.minecraft.world.entity.ItemOwner;

public interface LecternBlockEntityExt {

	default ItemOwner sparkweave$asItemOwner() {
		throw new AssertionError("Implemented in Mixin");
	}
}
