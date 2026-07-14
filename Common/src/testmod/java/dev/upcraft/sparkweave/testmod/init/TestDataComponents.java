package dev.upcraft.sparkweave.testmod.init;

import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import dev.upcraft.sparkweave.api.registry.datacomponent.DataComponentRegistryHandler;
import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import dev.upcraft.sparkweave.testmod.datacomponent.Openable;
import net.minecraft.core.component.DataComponentType;

public class TestDataComponents {

	public static final DataComponentRegistryHandler DATA_COMPONENTS = RegistryHandler.dataComponents(SparkweaveTestmod.MODID);
	public static final RegistrySupplier<DataComponentType<Openable>> OPENABLE = DATA_COMPONENTS.register("openable", builder -> builder.persistent(Openable.CODEC).networkSynchronized(Openable.STREAM_CODEC).ignoreSwapAnimation());
}
