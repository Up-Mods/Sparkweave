package dev.upcraft.sparkweave.api.registry.datacomponent;

import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.component.DataComponentType;

import java.util.function.UnaryOperator;

public interface DataComponentRegistryHandler extends RegistryHandler<DataComponentType<?>> {

	<T> RegistrySupplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> factory);
}
