package dev.upcraft.sparkweave.client.render;

import dev.upcraft.sparkweave.api.SparkweaveApi;
import dev.upcraft.sparkweave.logging.SparkweaveLogging;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class RenderDocHelper {

	private static final Logger LOGGER = SparkweaveLogging.getLogger();

	public static void init() {
		if(SparkweaveApi.Client.LOAD_RENDERDOC) {
			var libraryPath = System.getProperty("java.library.path");

			var rdHome = System.getenv("RENDERDOC_HOME");
			if(rdHome != null) {
				LOGGER.debug("appending RENDERDOC_HOME to native library path");
				libraryPath += File.pathSeparatorChar + rdHome;
				System.setProperty("java.library.path", libraryPath);
			}

			var libraryName = System.mapLibraryName("renderdoc");

			var loaded = false;
			for(var dir : libraryPath.split(File.pathSeparator)) {
				try {
					var searchPath = Path.of(dir, libraryName).toAbsolutePath();
					if(Files.exists(searchPath)) {
						LOGGER.debug("Attempting to load RenderDoc from {}", searchPath);
						System.load(searchPath.toString());
						loaded = true;
						break;
					}
				} catch (SecurityException | UnsatisfiedLinkError e) {
					LOGGER.error("unable to load RenderDoc library", e);
					break;
				}
			}

			if(!loaded) {
				LOGGER.warn("RenderDoc not found or unable to load");
			}
		}
	}
}
