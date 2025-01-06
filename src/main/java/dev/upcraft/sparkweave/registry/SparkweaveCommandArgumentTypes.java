package dev.upcraft.sparkweave.registry;

import dev.upcraft.sparkweave.SparkweaveHelper;
import dev.upcraft.sparkweave.api.command.CommandHelper;
import dev.upcraft.sparkweave.api.command.argument.RegistryArgumentType;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;

public class SparkweaveCommandArgumentTypes {

	public static final RegistryHandler<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = RegistryHandler.create(Registries.COMMAND_ARGUMENT_TYPE, SparkweaveHelper.MODID);

	public static final RegistrySupplier<ArgumentTypeInfo<RegistryArgumentType, ?>> REGISTRY = ARGUMENT_TYPES.register("registry", () -> CommandHelper.createArgumentInfo(RegistryArgumentType.class, SingletonArgumentInfo.contextFree(RegistryArgumentType::registry)));
}
