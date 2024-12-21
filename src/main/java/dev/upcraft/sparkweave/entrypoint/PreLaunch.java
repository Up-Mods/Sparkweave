package dev.upcraft.sparkweave.entrypoint;

import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.util.DotEnv;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.nio.file.Files;
import java.nio.file.Path;

@CalledByReflection
public class PreLaunch implements PreLaunchEntrypoint {

	static {
		var envFile = Path.of(".env");
		if (Files.isRegularFile(envFile)) {
			DotEnv.load(envFile);
		}
	}

	@Override
	public void onPreLaunch() {

	}
}
