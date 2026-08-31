package dev.upcraft.sparkweave.mixin.datagen;

import dev.upcraft.sparkweave.api.ext.datagen.TagAppenderExt;
import net.minecraft.data.tags.TagAppender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(TagAppender.class)
public interface TagAppenderMixin<E, T> extends TagAppenderExt<E, T> {

	@Shadow
	TagAppender<E, T> add(E element);

	@Shadow
	TagAppender<E, T> addAll(Stream<E> elements);

	@Override
	default TagAppender<E, T> add(Supplier<? extends E> element) {
		return this.add(element.get());
	}

	@SuppressWarnings("unchecked")
	@Override
	default TagAppender<E, T> add(Supplier<? extends E>... elements) {
		return this.addAll(Arrays.stream(elements).map(Supplier::get));
	}
}
