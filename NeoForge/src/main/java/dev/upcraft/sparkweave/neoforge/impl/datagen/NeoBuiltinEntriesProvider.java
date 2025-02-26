package dev.upcraft.sparkweave.neoforge.impl.datagen;

import dev.upcraft.sparkweave.api.datagen.provider.DynamicRegistryEntryProvider;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class NeoBuiltinEntriesProvider extends DatapackBuiltinEntriesProvider {

	private final CompletableFuture<HolderLookup.Provider> registriesFuture;
	private final Set<String> modIds;
	private final Map<ModContainer, List<DynamicRegistryEntryProvider>> providers;

	public NeoBuiltinEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, RegistrySetBuilder registrySetBuilder, Set<String> modIds, Map<ModContainer, List<DynamicRegistryEntryProvider>> providers) {
		super(output, registriesFuture, registrySetBuilder, modIds);
		this.registriesFuture = registriesFuture;
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
	public String getName() {
		return "Sparkweave Registries";
	}
}
