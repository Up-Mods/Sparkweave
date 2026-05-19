package dev.upcraft.sparkweave.api;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.platform.Env;
import dev.upcraft.sparkweave.api.platform.Services;
import org.apache.logging.log4j.Level;

public class SparkweaveApi {

	public static final boolean DEVELOPMENT_ENVIRONMENT = Services.PLATFORM.isDevelopmentEnvironment();

	public static final boolean CLIENTSIDE_ENVIRONMENT = Services.PLATFORM.getEnvironmentType().isClientSide();

	public static final boolean DEBUG_MODE = Env.getBool("debug", SparkweaveMod.MODID);

	public static final boolean DEBUG_LOGGING = DEBUG_MODE || Env.getBool("debug.logging", SparkweaveMod.MODID);

	public static final Level DEBUG_LOG_LEVEL = Level.toLevel(Env.get("debug.logging.level", SparkweaveMod.MODID), DEVELOPMENT_ENVIRONMENT ? Level.ALL : Level.DEBUG);

	public static final boolean DEVELOPER_CREATIVE_TAB = DEVELOPMENT_ENVIRONMENT || DEBUG_MODE || Env.getBool("developer.creative_tab.enabled", SparkweaveMod.MODID);

	public static class Client {

		// TODO remove in 26.1
		@Deprecated(forRemoval = true)
		public static final boolean LOAD_RENDERDOC = false;

		public static final boolean LOG_MISSING_TRANSLATIONS = DEVELOPMENT_ENVIRONMENT || Env.getBool("debug.log.missing_translations", SparkweaveMod.MODID);

		public static final boolean RENDER_SLOT_NUMBERS = DEBUG_MODE || Env.getBool("debug.render.slotnumber", SparkweaveMod.MODID);
	}
}
