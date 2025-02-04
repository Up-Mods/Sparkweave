package dev.upcraft.sparkweave.impl.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class RegistrySetBuilderDynamicProvider extends FabricDynamicRegistryProvider {

	private final Collection<RegistrySetBuilder> registrySetBuilders;
	private final ResourceLocation id;

	public RegistrySetBuilderDynamicProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, Collection<RegistrySetBuilder> registrySetBuilders, ResourceLocation id) {
		super(output, registriesFuture);
		this.registrySetBuilders = registrySetBuilders;
		this.id = id;
	}

	@Override
	protected void configure(HolderLookup.Provider registries, Entries entries) {
		registrySetBuilders.stream().flatMap(builder -> builder.getEntries().stream())
			.map(RegistrySetBuilder.RegistryStub::key)
			.distinct()
			.map(registries::lookupOrThrow)
			.forEach(entries::addAll);
	}

	@Override
	public String getName() {
		return id.toString();
	}
}
