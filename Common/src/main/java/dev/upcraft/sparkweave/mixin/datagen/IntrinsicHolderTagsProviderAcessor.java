package dev.upcraft.sparkweave.mixin.datagen;

import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Function;

@Mixin(IntrinsicHolderTagsProvider.class)
public interface IntrinsicHolderTagsProviderAcessor<T> {

	@Accessor("keyExtractor")
	Function<T, ResourceKey<T>> sparkweave$getKeyExtractor();
}
