package dev.upcraft.sparkweave.impl.service;

import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistryService;
import dev.upcraft.sparkweave.util.Utils;

@CalledByReflection
public class FabricRegistryService implements RegistryService {

	public FabricRegistryService() {
		// need an explicit default constructor for the service loader to work
	}

	@Override
	public <T> void registerAll(RegistryHandler<T> handler) {
		var registry = Utils.getBuiltinRegistry(handler.registry());
		handler.stream().forEach(supplier -> supplier.register(registry));
	}
}
