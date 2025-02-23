package dev.upcraft.sparkweave.entrypoint;

import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.PlatformUtils;
import dev.upcraft.sparkweave.api.reflect.ContextHelper;

import java.util.ServiceLoader;
import java.util.function.BiConsumer;

public class EntrypointHelper {

	public static <T> void fireEntrypoints(Class<T> clazz, BiConsumer<T, ModContainer> consumer, boolean validate) {
		ServiceLoader.load(clazz).forEach(instance -> {
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
