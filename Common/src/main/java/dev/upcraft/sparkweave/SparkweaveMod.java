package dev.upcraft.sparkweave;

import dev.upcraft.sparkweave.api.SparkweaveApi;
import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.event.CommandEvents;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.command.SparkweaveCommandRoot;
import dev.upcraft.sparkweave.registry.SparkweavePlacementModifiers;
import dev.upcraft.sparkweave.util.SparkweaveDevCreativeTab;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.stream.Stream;

public class SparkweaveMod implements MainEntryPoint {

	public static final String MODID = "sparkweave";

	@Override
	public void onInitialize(ModContainer mod) {
		CommandEvents.REGISTER.register(SparkweaveCommandRoot::register);
		var registryService = RegistryService.get();

		if(SparkweaveApi.DEVELOPER_CREATIVE_TAB) {
			var creativeTabsRegister = RegistryHandler.create(Registries.CREATIVE_MODE_TAB, SparkweaveMod.MODID);
			creativeTabsRegister.register(SparkweaveDevCreativeTab.DEVELOPER_MODE_TAB, SparkweaveDevCreativeTab::buildTab);
			creativeTabsRegister.accept(registryService);
		}

		SparkweavePlacementModifiers.MODIFIERS.accept(registryService);
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}

	public static List<ResourceLocation> ids(String... paths) {
		Validate.notEmpty(paths, "Must provide at least 1 ID!");
		return Stream.of(paths).map(SparkweaveMod::id).toList();
	}
}
