package dev.upcraft.sparkweave.neoforge.impl.datagen;

import dev.upcraft.sparkweave.api.datagen.DataGenerationContext;
import dev.upcraft.sparkweave.api.datagen.provider.DynamicRegistryEntryProvider;
import dev.upcraft.sparkweave.api.datagen.Pack;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NeoDataGenerationContext implements DataGenerationContext {

	private final ModContainer modContainer;
	private final GatherDataEvent event;
	private final NeoBuiltinPack builtinPack;

	public NeoDataGenerationContext(ModContainer modContainer, DataGenerator rootGenerator, CompletableFuture<HolderLookup.Provider> registriesFuture, GatherDataEvent event, List<DynamicRegistryEntryProvider> dynamicProviders) {
		this.modContainer = modContainer;
		this.event = event;
		this.builtinPack = new NeoBuiltinPack(this, rootGenerator, registriesFuture, dynamicProviders);
	}

	@Override
	public ModContainer getMod() {
		return modContainer;
	}

	@Override
	public boolean includeClient() {
		return event.includeClient();
	}

	@Override
	public boolean includeServer() {
		return event.includeServer();
	}

	@Override
	public boolean includeDev() {
		return event.includeDev();
	}

	@Override
	public boolean includeReports() {
		return event.includeReports();
	}

	@Override
	public boolean shouldValidate() {
		return event.validate();
	}

	@Override
	public Pack getDefaultPack() {
		return builtinPack;
	}
}
