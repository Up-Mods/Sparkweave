package dev.upcraft.sparkweave.fabric.service;

import com.google.auto.service.AutoService;
import com.google.common.base.Suppliers;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.RuntimeEnvironmentType;
import dev.upcraft.sparkweave.api.platform.services.PlatformService;
import dev.upcraft.sparkweave.fabric.impl.mod.FabricModContainer;
import dev.upcraft.sparkweave.platform.BasePlatformService;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.spongepowered.asm.util.JavaVersion;
import oshi.SystemInfo;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@AutoService(PlatformService.class)
public class FabricPlatformService extends BasePlatformService implements PlatformService {

	private static final String FABRIC_LOADER_MODID = "fabricloader";

	private final Map<String, Optional<ModContainer>> MOD_CONTAINERS = new Object2ObjectOpenHashMap<>();
	private final RuntimeEnvironmentType environmentType = switch (FabricLoader.getInstance().getEnvironmentType()) {
		case CLIENT -> RuntimeEnvironmentType.CLIENT;
		case SERVER -> RuntimeEnvironmentType.SERVER;
	};
	private final Supplier<String> userAgent = Suppliers.memoize(() -> {
		var info = new SystemInfo();
		var os = info.getOperatingSystem();

		var platformName = getPlatformName();
		var platformVersion = getModContainer(FABRIC_LOADER_MODID).orElseThrow(() -> new IllegalStateException("Unable to find fabric loader!")).metadata().version();

		var mcVersion = FabricLoaderImpl.INSTANCE.getGameProvider().getRawGameVersion();

		var jvmVendor = System.getProperty("java.vm.vendor");
		var jvmVersion = Runtime.version().toString();

		var osName = os.getFamily();
		var osVersion = os.getVersionInfo().getVersion();

		var bitness = "x" + os.getBitness();
		if (os.getBitness() == 32) {
			bitness = "x86";
		}

		return String.format("%s/%s Minecraft/%s Java/%.1f (%s/%s) (%s %s; %s)", platformName, platformVersion, mcVersion, JavaVersion.current(), jvmVendor, jvmVersion, osName, osVersion, bitness);
	});

	@CalledByReflection
	public FabricPlatformService() {
		// need an explicit default constructor for the service loader to work
		super();
	}

	@Override
	public boolean isModLoaded(String modid) {
		return FabricLoader.getInstance().isModLoaded(modid);
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Override
	public Path getGameDir() {
		return FabricLoader.getInstance().getGameDir();
	}

	@Override
	public Path getConfigDir() {
		return FabricLoader.getInstance().getConfigDir();
	}

	@Override
	public Optional<ModContainer> getModContainer(String modid) {
		return MOD_CONTAINERS.computeIfAbsent(modid, key -> FabricLoader.getInstance().getModContainer(key).map(FabricModContainer::of));
	}

	@Override
	public List<ModContainer> getActiveMods() {
		// can't use Stream#toList() because java compiler is dumb :(
		return FabricLoader.getInstance().getAllMods().stream().map(FabricModContainer::of).collect(Collectors.toList());
	}

	@Override
	public RuntimeEnvironmentType getEnvironmentType() {
		return environmentType;
	}

	@Override
	public List<String> getLaunchArguments(boolean hideSensitive) {
		return List.of(FabricLoader.getInstance().getLaunchArguments(hideSensitive));
	}

	@Override
	public String getUserAgent() {
		return userAgent.get();
	}

	@Override
	public String getPlatformName() {
		return "Fabric";
	}

}
