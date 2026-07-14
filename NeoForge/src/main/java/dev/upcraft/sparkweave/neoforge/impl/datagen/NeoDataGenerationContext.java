package dev.upcraft.sparkweave.neoforge.impl.datagen;

import dev.upcraft.sparkweave.api.datagen.DataGenerationContext;
import dev.upcraft.sparkweave.api.datagen.provider.SparkweaveDynamicRegistryEntryProvider;
import dev.upcraft.sparkweave.api.datagen.Pack;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.datagen.SparkweaveDatagenHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NeoDataGenerationContext implements DataGenerationContext {

	private final ModContainer modContainer;
	private final GatherDataEvent event;
	private final boolean includeClient;
	private final NeoBuiltinPack builtinPack;

	public NeoDataGenerationContext(ModContainer modContainer, DataGenerator rootGenerator, CompletableFuture<HolderLookup.Provider> registriesFuture, GatherDataEvent event, boolean includeClient, List<SparkweaveDynamicRegistryEntryProvider> dynamicProviders) {
		this.modContainer = modContainer;
		this.event = event;
		this.includeClient = includeClient;
		this.builtinPack = new NeoBuiltinPack(this, rootGenerator, registriesFuture, dynamicProviders);
	}

	@Override
	public ModContainer getMod() {
		return modContainer;
	}

	@Override
	public boolean includeClient() {
		return includeClient;
	}

	@Override
	public boolean includeDevTools() {
		return event.includeDev() || SparkweaveDatagenHelper.INCLUDE_DEV_TOOLS;
	}

	@Override
	public boolean includeReports() {
		return event.includeReports() || SparkweaveDatagenHelper.INCLUDE_REPORTS;
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
