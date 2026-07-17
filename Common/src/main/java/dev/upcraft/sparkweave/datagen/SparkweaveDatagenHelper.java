package dev.upcraft.sparkweave.datagen;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.SparkweaveApi;
import dev.upcraft.sparkweave.api.platform.Env;
import net.minecraft.util.Util;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SparkweaveDatagenHelper {

	public static final Set<String> ENABLED_MODS = Util.make(() -> {
		var modIDs = Arrays.stream(Env.get("datagen.mods", SparkweaveMod.MODID, "").strip().split(",\\s*"))
			.filter(Predicate.not(String::isBlank))
			.collect(Collectors.toCollection(TreeSet::new));

		if (modIDs.isEmpty()) {
			throw new IllegalArgumentException("[Sparkweave] sparkweave.datagen.mods property was empty or not set! please define which mod(s) to generate data for, as a comma-separated list!");
		}

		return Collections.unmodifiableSet(modIDs);
	});

	public static final boolean INCLUDE_DEV_TOOLS = SparkweaveApi.DEVELOPMENT_ENVIRONMENT || Env.getBool("datagen.generate.dev", SparkweaveMod.MODID);
	public static final boolean INCLUDE_REPORTS = SparkweaveApi.DEVELOPMENT_ENVIRONMENT || Env.getBool("datagen.generate.reports", SparkweaveMod.MODID);
}
