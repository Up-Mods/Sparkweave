package dev.upcraft.sparkweave.api.client.ext;

import net.minecraft.util.context.ContextKey;
import org.jspecify.annotations.Nullable;

public interface RenderStateExt {

	default <T> void sparkweave$setData(ContextKey<T> key, @Nullable T data) {
		throw new AssertionError("Implemented in Mixin");
	}

	@Nullable
	default <T> T sparkweave$getData(ContextKey<T> key) {
		throw new AssertionError("Implemented in Mixin");
	}
}
