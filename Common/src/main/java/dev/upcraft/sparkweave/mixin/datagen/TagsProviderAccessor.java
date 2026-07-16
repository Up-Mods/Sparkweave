package dev.upcraft.sparkweave.mixin.datagen;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TagsProvider.class)
public interface TagsProviderAccessor<T> {

	@Invoker("getOrCreateRawBuilder")
	TagBuilder sparkweave$getOrCreateRawBuilder(TagKey<T> tag);
}
