package dev.upcraft.sparkweave.api.datagen;

import dev.upcraft.sparkweave.api.platform.ModContainer;

public interface DataGenerationContext {

	ModContainer getMod();

	boolean includeClient();

	boolean includeDevTools();

	boolean includeReports();

	boolean shouldValidate();

	/**
	 * @return the default data/resource pack for this mod
	 */
	Pack getDefaultPack();

	// TODO builtin resource/data packs
}
