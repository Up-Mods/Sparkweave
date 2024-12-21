package dev.upcraft.sparkweave.api.util.logging;

import dev.upcraft.sparkweave.api.annotation.CallerSensitive;
import dev.upcraft.sparkweave.api.util.ContextHelper;
import net.fabricmc.loader.api.ModContainer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

public class SparkweaveLoggerFactory {

	private static final LoggerContext ctx = Configurator.initialize(SparkweaveLoggerFactory.class.getClassLoader(), ConfigurationSource.fromResource("/assets/sparkweave/log4j2.xml", SparkweaveLoggerFactory.class.getClassLoader()));

	private SparkweaveLoggerFactory() {
	}

	public static Logger getLogger(String name) {
		return ctx.getLogger(name);
	}

	public static Logger getLogger(ModContainer mod) {
		return getLogger(mod.getMetadata().getName());
	}

	@CallerSensitive
	public static Logger getLogger() {
		return getLogger(ContextHelper.getCallerContext());
	}



}
