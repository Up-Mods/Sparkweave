package dev.upcraft.sparkweave.api.platform;

import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.services.PlatformService;
import org.apache.logging.log4j.Logger;

import java.util.ServiceLoader;

public class Services {

	private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger("Sparkweave Engine/ServiceLoader");

	public static <T> T getService(Class<T> serviceClass) {
		// explicitly pass the class loader so we don't fall back to the service class loader
		T service = ServiceLoader.load(serviceClass, Services.class.getClassLoader()).findFirst().orElseThrow(() -> new IllegalStateException("No platform implementation found for " + serviceClass.getName()));
		LOGGER.debug("Loaded {} for service {}", service.getClass().getName(), serviceClass.getName());
		return service;
	}

	public static final PlatformService PLATFORM = getService(PlatformService.class);
}
