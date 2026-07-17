package dev.upcraft.sparkweave.fabric.impl.datagen;

import dev.upcraft.sparkweave.api.SparkweaveApi;
import dev.upcraft.sparkweave.api.datagen.DataGenerationContext;
import dev.upcraft.sparkweave.api.datagen.Pack;
import dev.upcraft.sparkweave.api.datagen.provider.common.dynamic.SparkweaveDynamicRegistryEntryProvider;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.datagen.SparkweaveDatagenHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.HolderLookup;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FabricDataGenerationContext implements DataGenerationContext {

	private final ModContainer modContainer;
	private final FabricBuiltinPack builtinPack;
	private final boolean validate;

	public FabricDataGenerationContext(ModContainer modContainer, boolean validate, FabricDataGenerator.Pack fabricPack, CompletableFuture<HolderLookup.Provider> registriesFuture, List<SparkweaveDynamicRegistryEntryProvider> dynamicProviders) {
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
		return SparkweaveApi.CLIENTSIDE_ENVIRONMENT;
	}

	@Override
	public boolean includeDevTools() {
		return SparkweaveDatagenHelper.INCLUDE_DEV_TOOLS;
	}

	@Override
	public boolean includeReports() {
		return SparkweaveDatagenHelper.INCLUDE_REPORTS;
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
