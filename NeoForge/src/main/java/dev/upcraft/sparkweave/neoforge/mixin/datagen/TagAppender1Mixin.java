package dev.upcraft.sparkweave.neoforge.mixin.datagen;

import dev.upcraft.sparkweave.api.ext.datagen.TagAppenderExt;
import dev.upcraft.sparkweave.neoforge.impl.datagen.ForcedTagEntry;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net/minecraft/data/tags/TagAppender$1")
public abstract class TagAppender1Mixin<T> implements TagAppenderExt<ResourceKey<T>, T> {
	@Shadow
	@Final
	TagBuilder val$builder;

	@SuppressWarnings("unchecked")
	@Override
	public TagAppender<ResourceKey<T>, T> addExistingTag(TagKey<T> tag) {
		this.val$builder.add(new ForcedTagEntry(tag.location()));
		return (TagAppender<ResourceKey<T>, T>) this;
	}
}
