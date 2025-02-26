package dev.upcraft.sparkweave.fabric.impl.datagen;

import dev.upcraft.sparkweave.api.datagen.provider.DynamicRegistryEntryProvider;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.CachedOutput;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class FabricBuiltinEntriesProvider extends FabricDynamicRegistryProvider {

	private final CompletableFuture<HolderLookup.Provider> registriesFuture;
	private final Set<String> modIds;
	private final Map<ModContainer, List<DynamicRegistryEntryProvider>> providers;
	private final RegistrySetBuilder registrySetBuilder;

	public FabricBuiltinEntriesProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, RegistrySetBuilder registrySetBuilder, Set<String> modIds, Map<ModContainer, List<DynamicRegistryEntryProvider>> providers) {
		super(output, registriesFuture);
		this.registriesFuture = registriesFuture;
		this.registrySetBuilder = registrySetBuilder;
		this.modIds = modIds;
		this.providers = providers;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		var parent = super.run(output);
		var extraContent = registriesFuture.thenCompose(registries -> CompletableFuture.allOf(providers.entrySet().stream().filter(e -> modIds.contains(e.getKey().metadata().id())).flatMap(e -> e.getValue().stream()).map(provider -> provider.generateEntryData(output, registries)).toArray(CompletableFuture[]::new)));
		return CompletableFuture.allOf(parent, extraContent);
	}

	@Override
	protected void configure(HolderLookup.Provider registries, Entries entries) {
		// cannot simply use entries#addAll here because that filters by active fabric mod container
		registrySetBuilder.getEntryKeys().stream().map(registries::lookupOrThrow).forEach(registry -> {
			registry.listElementIds().filter(registryKey -> modIds.contains(registryKey.location().getNamespace())).forEach(key -> entries.add(registry, key));
		});
	}

	@Override
	public String getName() {
		return "Sparkweave Registries";
	}
}
