package dev.upcraft.sparkweave.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

import java.util.Objects;

public class RegistryHelper {

	@SuppressWarnings("unchecked")
	public static <T> Registry<T> getBuiltinRegistry(ResourceKey<Registry<T>> registryKey) {
		return Objects.requireNonNull(((Registry<Registry<T>>) BuiltInRegistries.REGISTRY).getValue(registryKey), "unable to resolve registry" + registryKey);
	}

}
