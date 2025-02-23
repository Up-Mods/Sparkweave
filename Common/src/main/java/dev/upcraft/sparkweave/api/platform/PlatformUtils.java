package dev.upcraft.sparkweave.api.platform;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class PlatformUtils {

	/**
	 * see net.fabricmc.loader.impl.metadata.MetadataVerifier
	 * (added {@code ^$} because fabric verifies that the whole string matches anyway)
	 */
	private static final Pattern FABRIC_MOD_ID_PATTERN = Pattern.compile("^[a-z][a-z0-9-_]{1,63}$");

	/**
	 * see net.neoforged.fml.loading.moddiscovery.ModInfo
	 */
	private static final Pattern NEOFORGE_MOD_ID_PATTERN = Pattern.compile("^[a-z][a-z0-9_]{1,63}$");

	/**
	 * @return whether the given mod ID is valid on all supported platforms
	 */
	public static boolean isValidModId(String modid) {
		// fabric:
		if(!FABRIC_MOD_ID_PATTERN.asMatchPredicate().test(modid)) {
			return false;
		}

		// neoforge:
		if(!NEOFORGE_MOD_ID_PATTERN.asMatchPredicate().test(modid)) {
			return false;
		}

		// vanilla:
		return ResourceLocation.isValidNamespace(modid);
	}

	public static void assertValidModId(String modid) {
		var validVanilla = ResourceLocation.isValidNamespace(modid);
		var validFabric = FABRIC_MOD_ID_PATTERN.asMatchPredicate().test(modid);
		var validNeoForge = NEOFORGE_MOD_ID_PATTERN.asMatchPredicate().test(modid);

		if(!validVanilla || !validFabric || !validNeoForge) {
			Set<String> failed = new TreeSet<>();
			if(!validVanilla) { failed.add("Vanilla"); }
			if(!validFabric) { failed.add("Fabric"); }
			if(!validNeoForge) { failed.add("NeoForge"); }
			throw new AssertionError("Mod ID '%s' is invalid for the following platforms: %s".formatted(modid, String.join(", ", failed)));
		}
	}
}
