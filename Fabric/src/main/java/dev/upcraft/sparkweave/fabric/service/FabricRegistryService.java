package dev.upcraft.sparkweave.fabric.service;

import com.google.auto.service.AutoService;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistryVisitor;
import dev.upcraft.sparkweave.fabric.impl.registry.FabricRegistryHandler;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.stream.Collectors;

@AutoService(RegistryService.class)
public class FabricRegistryService implements RegistryService {

	@CalledByReflection
	public FabricRegistryService() {
		// need an explicit default constructor for the service loader to work
	}

	@Override
	public <T> RegistryHandler<T> createRegistryHandler(ResourceKey<Registry<T>> registryKey, String namespace) {
		return FabricRegistryHandler.create(registryKey, namespace);
	}

	@Override
	public <T> void visitRegistry(Registry<T> registry, RegistryVisitor<T> callback) {
		RegistryEntryAddedCallback.event(registry).register((rawId, id, object) -> callback.accept(id, object));
		// need to create a copy because the callback itself may add entries to the registry while we are iterating it
		var copy = registry.holders().collect(Collectors.toMap(holder -> holder.key().location(), Holder.Reference::value));
		copy.forEach(callback);
	}

	@Override
	public <T> void handleRegister(RegistryHandler<T> handler) {
		// NO-OP because registration happens anyway, we just need to load the class
	}
}
