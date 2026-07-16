package dev.upcraft.sparkweave.neoforge.mixin.datagen;

import net.minecraft.data.tags.TagsProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TagsProvider.class)
public interface TagsProviderAccessor {

	@Mutable
	@Accessor("modId")
	void sparkweave$setModId(String value);
}
