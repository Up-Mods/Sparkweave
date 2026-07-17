package dev.upcraft.sparkweave.fabric.mixin.datagen;

import dev.upcraft.sparkweave.api.ext.datagen.TagAppenderExt;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagAppender;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TagAppender.class)
public interface TagAppenderMixin<E, T> extends FabricTagAppender<E, T>, TagAppenderExt<E, T> {

	@Override
	default TagAppender<E, T> addExistingTag(TagKey<T> tag) {
		return forceAddTag(tag);
	}
}
