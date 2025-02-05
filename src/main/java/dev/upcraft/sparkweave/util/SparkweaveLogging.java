package dev.upcraft.sparkweave.util;

import dev.upcraft.sparkweave.api.util.logging.SparkweaveLoggerFactory;
import org.apache.logging.log4j.Logger;

public class SparkweaveLogging {

	@Deprecated(forRemoval = true)
    public static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();

    public static Logger getLogger() {
        return LOGGER;
    }
}
