package dev.upcraft.sparkweave.util;

import com.google.common.base.Preconditions;
import net.fabricmc.loader.impl.metadata.MetadataVerifier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

import java.util.Objects;
import java.util.regex.Pattern;

public class Utils {

	/**
	 * @see MetadataVerifier
	 */
	public static final Pattern FABRIC_MOD_ID_PATTERN = Pattern.compile("[a-z][a-z0-9-_]{1,63}");

	@SuppressWarnings("unchecked")
	public static <T> Registry<T> getBuiltinRegistry(ResourceKey<Registry<T>> registryKey) {
		return Objects.requireNonNull(((Registry<Registry<T>>) BuiltInRegistries.REGISTRY).get(registryKey), "unable to resolve registry" + registryKey);
	}

	public static boolean isValidFabricModId(String modid) {
		return FABRIC_MOD_ID_PATTERN.asMatchPredicate().test(modid);
	}

	public static void assertValidFabricModId(String modid) {
		Preconditions.checkArgument(isValidFabricModId(modid), "Invalid mod id: %s", modid);
	}
}
