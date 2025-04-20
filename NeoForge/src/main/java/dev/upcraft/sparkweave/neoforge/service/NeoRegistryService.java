package dev.upcraft.sparkweave.neoforge.service;

import com.google.auto.service.AutoService;
import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistryVisitor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.NotImplementedException;

@AutoService(RegistryService.class)
public class NeoRegistryService implements RegistryService {

	private final IEventBus modBus;

	@CalledByReflection
	public NeoRegistryService() {
		// need an explicit default constructor for the service loader to work

		modBus = ModList.get().getModContainerById(SparkweaveMod.MODID).orElseThrow().getEventBus();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> RegistryHandler<T> createRegistryHandler(ResourceKey<Registry<T>> registryKey, String namespace) {
		return (RegistryHandler<T>) DeferredRegister.create(registryKey, namespace);
	}

	@Override
	public <T> void visitRegistry(Registry<T> registry, RegistryVisitor<T> callback) {
		throw new NotImplementedException();
	}

	@Override
	public <T> void handleRegister(RegistryHandler<T> handler) {
		if (handler instanceof DeferredRegister<?> deferredRegister) {
			deferredRegister.register(modBus);
		}
	}
}
