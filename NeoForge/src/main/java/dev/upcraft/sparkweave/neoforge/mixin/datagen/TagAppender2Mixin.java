package dev.upcraft.sparkweave.neoforge.mixin.datagen;

import dev.upcraft.sparkweave.api.ext.datagen.TagAppenderExt;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net/minecraft/data/tags/TagAppender$2")
public abstract class TagAppender2Mixin<U, E, T> implements TagAppenderExt<U, T> {
	@Shadow
	@Final
	TagAppender<E, T> val$original;

	@SuppressWarnings("unchecked")
	@Override
	public TagAppender<U, T> addExistingTag(TagKey<T> tag) {
		this.val$original.addExistingTag(tag);
		return (TagAppender<U, T>) this;
	}
}
