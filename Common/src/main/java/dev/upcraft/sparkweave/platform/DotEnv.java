package dev.upcraft.sparkweave.platform;

import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DotEnv {

	public static void load(Path path) {
		if(!Files.isRegularFile(path)) {
			return;
		}
		try (var reader = Files.newBufferedReader(path)) {
			System.getProperties().load(reader);
		} catch (IOException e) {
			SparkweaveLoggerFactory.getLogger("Sparkweave Engine/Env").error("Unable to load env file: {}", path.toAbsolutePath(), e);
		}
	}

	/**
	 * loads in the following order (later means it overwrites values from previous files):
	 * <ol>
	 *     <li>{@code .env}</li>
	 *     <li>{@code $side.env}</li>
	 *     <li>{@code $side.$environment.env}</li>
	 * </ol>
	 *
	 * {@code $side} is the value of the current {@link dev.upcraft.sparkweave.api.platform.RuntimeEnvironmentType},
	 * {@code $environment} is either {@code production} or {@code development}
	 */
	public static void load() {
		load(Path.of(".env"));
		load(Path.of(String.format("%s.env", Services.PLATFORM.getEnvironmentType())));
		load(Path.of(String.format("%s.%s.env", Services.PLATFORM.getEnvironmentType(), Services.PLATFORM.getEnvString())));
	}
}
