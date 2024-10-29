package dev.upcraft.sparkweave;

import dev.upcraft.sparkweave.api.SparkweaveApi;
import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.event.CommandEvents;
import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.command.SparkweaveCommandRoot;
import dev.upcraft.sparkweave.util.SparkweaveDevCreativeTab;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Stream;

public class SparkweaveMod implements MainEntryPoint {

	public static final String MODID = "sparkweave";
	private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();

	@Override
	public void onInitialize(ModContainer mod) {
		CommandEvents.REGISTER.register(SparkweaveCommandRoot::register);
		var service = RegistryService.get();

		if(SparkweaveApi.DEVELOPER_CREATIVE_TAB) {
			var creativeTabsRegister = RegistryHandler.create(Registries.CREATIVE_MODE_TAB, SparkweaveMod.MODID);
			creativeTabsRegister.register(SparkweaveDevCreativeTab.DEVELOPER_MODE_TAB, SparkweaveDevCreativeTab::buildTab);
			creativeTabsRegister.accept(service);
		}
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}

	public static List<ResourceLocation> ids(String... paths) {
		Validate.notEmpty(paths, "Must provide at least 1 ID!");
		return Stream.of(paths).map(SparkweaveMod::id).toList();
	}

	public static void onLoadComplete() {
		LOGGER.debug("System initialized!");
	}
}
