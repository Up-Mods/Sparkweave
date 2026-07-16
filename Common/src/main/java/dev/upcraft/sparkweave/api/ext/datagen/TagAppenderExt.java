package dev.upcraft.sparkweave.api.ext.datagen;

import net.minecraft.data.tags.TagAppender;

import java.util.function.Supplier;

public interface TagAppenderExt<E, T> {

	default TagAppender<E, T> add(Supplier<E> element) {
		throw new AssertionError("Implemented in Mixin");
	}

	@SuppressWarnings("unchecked")
	default TagAppender<E, T> add(Supplier<E>... elements) {
		throw new AssertionError("Implemented in Mixin");
	}
}
