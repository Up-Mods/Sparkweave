package dev.upcraft.sparkweave.neoforge.entrypoint;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.entrypoint.DedicatedServerEntryPoint;
import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.event.CustomLecternMenuEvent;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.api.registry.block.BlockItemProvider;
import dev.upcraft.sparkweave.entrypoint.EntrypointHelper;
import dev.upcraft.sparkweave.logging.SparkweaveLogging;
import dev.upcraft.sparkweave.registry.SparkweaveCommandArgumentTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.RegisterEvent;

@CalledByReflection
@Mod(SparkweaveMod.MODID)
public class Main {

	public Main(IEventBus bus) {
		bus.register(this);

		var helper = RegistryService.get();
		SparkweaveCommandArgumentTypes.ARGUMENT_TYPES.accept(helper);

		EntrypointHelper.fireEntrypoints(MainEntryPoint.class, MainEntryPoint::onInitialize);

		CustomLecternMenuEvent.EVENT.invoker().registerLecternMenus(new CustomLecternMenuEvent());

		switch (FMLEnvironment.dist) {
			case CLIENT ->
				EntrypointHelper.fireEntrypoints(ClientEntryPoint.class, ClientEntryPoint::onInitializeClient);
			case DEDICATED_SERVER ->
				EntrypointHelper.fireEntrypoints(DedicatedServerEntryPoint.class, DedicatedServerEntryPoint::onInitializeServer);
		}

		SparkweaveLogging.getLogger().debug("System initialized!");
	}

	@SubscribeEvent
	public void processBlockItems(RegisterEvent event) {
		if (event.getRegistryKey() == Registries.ITEM) {
			BuiltInRegistries.BLOCK.entrySet().forEach(entry -> {
				if (entry.getValue() instanceof BlockItemProvider provider) {
					event.register(Registries.ITEM, entry.getKey().location(), provider::createItem);
				}
			});
		}
	}
}
