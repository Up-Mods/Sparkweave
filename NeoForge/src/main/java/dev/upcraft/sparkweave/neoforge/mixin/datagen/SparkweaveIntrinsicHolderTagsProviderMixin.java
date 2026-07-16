package dev.upcraft.sparkweave.neoforge.mixin.datagen;

import dev.upcraft.sparkweave.api.datagen.provider.SparkweaveIntrinsicHolderTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Mixin(SparkweaveIntrinsicHolderTagsProvider.class)
public abstract class SparkweaveIntrinsicHolderTagsProviderMixin<T> extends IntrinsicHolderTagsProvider<T> implements TagsProviderAccessor {

	private SparkweaveIntrinsicHolderTagsProviderMixin(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, Function<T, ResourceKey<T>> keyExtractor, String modId) {
		super(output, registryKey, lookupProvider, keyExtractor, modId);
	}

	@Inject(method = "<init>(Lnet/minecraft/data/PackOutput;Lnet/minecraft/resources/ResourceKey;Ljava/lang/String;Ljava/util/concurrent/CompletableFuture;Ljava/util/concurrent/CompletableFuture;Ljava/util/function/Function;)V", at = @At("RETURN"))
	private void construct(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, String modId, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<T>> parentProvider, Function<T, ResourceKey<T>> keyExtractor, CallbackInfo ci) {
		this.sparkweave$setModId(modId);
	}
}
