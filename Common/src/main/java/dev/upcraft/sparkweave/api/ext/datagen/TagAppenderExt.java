package dev.upcraft.sparkweave.api.ext.datagen;

import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;

import java.util.function.Supplier;

public interface TagAppenderExt<E, T> {

	default TagAppender<E, T> add(Supplier<? extends E> element) {
		throw new AssertionError("Implemented in Mixin");
	}

	@SuppressWarnings("unchecked")
	default TagAppender<E, T> add(Supplier<? extends E>... elements) {
		throw new AssertionError("Implemented in Mixin");
	}

	default TagAppender<E, T> addExistingTag(TagKey<T> tag) {
		throw new AssertionError("Implemented in Mixin");
	}
}
