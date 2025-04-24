package dev.upcraft.sparkweave.validation;

import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.logging.SparkweaveLogging;
import net.minecraft.server.Bootstrap;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TranslationChecker {

	private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();
	private static final Set<String> MISSING_KEYS = new HashSet<>();
	private static final Set<String> MISSING_KEYS_VIEW = Collections.unmodifiableSet(MISSING_KEYS);

	private static void notifyMissingTranslation(String translationKey) {
		if(!MISSING_KEYS.contains(translationKey)) {
			SparkweaveLogging.getLogger().warn("Missing translation for key '{}'", translationKey);
			MISSING_KEYS.add(translationKey);
		}
	}

	public static void validate() {
		LOGGER.info("Validating translations...");
		Bootstrap.getMissingTranslations().forEach(TranslationChecker::notifyMissingTranslation);
	}

	public static Set<String> getMissingKeys() {
		return MISSING_KEYS_VIEW;
	}
}
