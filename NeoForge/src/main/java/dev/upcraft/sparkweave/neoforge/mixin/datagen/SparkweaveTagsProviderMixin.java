package dev.upcraft.sparkweave.neoforge.mixin.datagen;

import dev.upcraft.sparkweave.api.datagen.provider.SparkweaveTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(SparkweaveTagsProvider.class)
public abstract class SparkweaveTagsProviderMixin<T> implements DataProvider, TagsProviderAccessor {

	@Inject(method = "<init>(Lnet/minecraft/data/PackOutput;Lnet/minecraft/resources/ResourceKey;Ljava/lang/String;Ljava/util/concurrent/CompletableFuture;Ljava/util/concurrent/CompletableFuture;)V", at = @At("RETURN"))
	private void construct(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<T>> parentProvider, CallbackInfo ci) {
		this.sparkweave$setModId(modId);
	}
}
