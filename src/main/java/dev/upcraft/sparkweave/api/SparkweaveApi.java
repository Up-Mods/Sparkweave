package dev.upcraft.sparkweave.api;

import dev.upcraft.sparkweave.api.util.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;

public class SparkweaveApi {

	public static final boolean DEVELOPMENT_ENVIRONMENT = FabricLoader.getInstance().isDevelopmentEnvironment();

	public static final boolean CLIENTSIDE_ENVIRONMENT = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;

	public static final boolean DEBUG_MODE = Env.getBool("debug");

	public static final boolean DEBUG_LOGGING = DEBUG_MODE || Env.getBool("debug.logging");

	public static final Level DEBUG_LOG_LEVEL = Level.toLevel(Env.get("debug.logging.level"), DEVELOPMENT_ENVIRONMENT ? Level.ALL : Level.DEBUG);

	@Environment(EnvType.CLIENT)
	public static class Client {

		public static final boolean RENDER_SLOT_NUMBERS = DEBUG_MODE || Env.getBool("debug.render.slotnumber");

		public static final boolean LOG_MISSING_TRANSLATIONS = DEVELOPMENT_ENVIRONMENT || Env.getBool("debug.log.missing_translations");
	}
}
