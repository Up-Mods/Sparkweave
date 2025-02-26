package dev.upcraft.sparkweave.fabric.impl.datagen;

import dev.upcraft.sparkweave.api.datagen.DataGenerationContext;
import dev.upcraft.sparkweave.api.datagen.provider.DynamicRegistryEntryProvider;
import dev.upcraft.sparkweave.api.datagen.Pack;
import dev.upcraft.sparkweave.api.SparkweaveApi;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.HolderLookup;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FabricDataGenerationContext implements DataGenerationContext {

	private final ModContainer modContainer;
	private final FabricBuiltinPack builtinPack;
	private final boolean validate;

	public FabricDataGenerationContext(ModContainer modContainer, boolean validate, FabricDataGenerator.Pack fabricPack, CompletableFuture<HolderLookup.Provider> registriesFuture, List<DynamicRegistryEntryProvider> dynamicProviders) {
		this.modContainer = modContainer;
		this.validate = validate;
		this.builtinPack = new FabricBuiltinPack(this, fabricPack, registriesFuture, dynamicProviders);
	}

	@Override
	public ModContainer getMod() {
		return modContainer;
	}

	@Override
	public boolean includeClient() {
		// FIXME better check here?
		return SparkweaveApi.CLIENTSIDE_ENVIRONMENT;
	}

	@Override
	public boolean includeServer() {
		// FIXME better check here?
		return true;
	}

	@Override
	public boolean includeDev() {
		// FIXME better check here?
		return SparkweaveApi.DEVELOPMENT_ENVIRONMENT;
	}

	@Override
	public boolean includeReports() {
		// FIXME better check here?
		return true;
	}

	@Override
	public boolean shouldValidate() {
		return validate;
	}

	@Override
	public Pack getDefaultPack() {
		return builtinPack;
	}
}
