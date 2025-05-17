package dev.upcraft.sparkweave.logging;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.annotation.Mod;
import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import org.apache.logging.log4j.Logger;

@Deprecated(forRemoval = true)
@Mod.Context(SparkweaveMod.MODID)
public class SparkweaveLogging {
    private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();

    public static Logger getLogger() {
        return LOGGER;
    }
}
