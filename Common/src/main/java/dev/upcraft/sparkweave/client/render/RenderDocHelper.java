package dev.upcraft.sparkweave.client.render;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class RenderDocHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(RenderDocHelper.class);
	public static final boolean LOAD_RENDERDOC = Boolean.getBoolean("sparkweave.debug.render.load_renderdoc");

	public static void init() {
		if(LOAD_RENDERDOC) {
			LOGGER.info("Loading RenderDoc");
			var libraryPath = System.getProperty("java.library.path");

			// if set, search in RENDERDOC_HOME first, then fall back to library path
			var rdHome = System.getenv("RENDERDOC_HOME");
			if(rdHome != null) {
				libraryPath = rdHome + File.pathSeparator + libraryPath;
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
