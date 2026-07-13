package dev.upcraft.sparkweave.entrypoint;

import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.PlatformUtils;
import dev.upcraft.sparkweave.api.reflect.ContextHelper;
import org.apache.logging.log4j.Logger;

import java.util.ServiceLoader;
import java.util.function.BiConsumer;

public class EntrypointHelper {

	private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();

	public static <T> void fireEntrypoints(Class<T> clazz, BiConsumer<T, ModContainer> consumer, boolean validate) {
		LOGGER.debug("Firing entrypoint {}", clazz.getName());
		ServiceLoader.load(clazz, EntrypointHelper.class.getClassLoader()).forEach(instance -> {
			LOGGER.debug("Entrypoint {}: Found candidate {}", clazz.getName(), instance.getClass().getName());
			ModContainer modContainer = ContextHelper.getContext(instance.getClass());
			if(modContainer == null) {
				throw new IllegalStateException("Entrypoint instance " + instance.getClass().getName() + " does not have a mod container");
			}

			if(validate) {
				PlatformUtils.assertValidModId(modContainer.metadata().id());
			}

			consumer.accept(instance, modContainer);
		});
	}

	public static <T> void fireEntrypoints(Class<T> clazz, BiConsumer<T, ModContainer> consumer) {
		fireEntrypoints(clazz, consumer, true);
	}


}
