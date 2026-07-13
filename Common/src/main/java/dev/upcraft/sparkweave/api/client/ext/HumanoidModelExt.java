package dev.upcraft.sparkweave.api.client.ext;

import net.minecraft.client.model.HumanoidModel;

public interface HumanoidModelExt {

	default void sparkweave$copyPropertiesTo(HumanoidModel<?> other) {
		throw new AssertionError("Implemented in Mixin");
	}
}
