package dev.upcraft.sparkweave.neoforge.impl.client;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.ModLoadingIssue;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.loading.progress.StartupNotificationManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;

@Mod(value = SparkweaveMod.MODID, dist = Dist.CLIENT)
public class LaunchBootstrap {

	private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();

	private static final Boolean LOAD_RENDERDOC = Boolean.getBoolean("sparkweave.debug.render.load_renderdoc");

	public LaunchBootstrap(ModContainer modContainer, IEventBus modBus) throws IOException {
		var currentModVersion = modContainer.getModInfo().getVersion().toString();
		if(LOAD_RENDERDOC) {
			var pb = StartupNotificationManager.prependProgressBar("Extracting RenderDoc", 2);

			var helperClass = getClass("dev.upcraft.sparkweave.renderdoc.client.RenderDocHelper");
			if(helperClass != null) {
				try {
					var isLoadedMethod = MethodHandles.publicLookup().findStatic(helperClass, "isLoaded", MethodType.methodType(boolean.class));
					if((boolean) isLoadedMethod.invokeExact()) {
						LOGGER.info("RenderDoc already loaded, skipping jar extraction!");
						pb.complete();
						return;
					}
				} catch (Throwable e) {
					throw new RuntimeException("Reflection error trying to check RenderDocHelper API", e);
				}
			}

			LOGGER.debug("Locating RenderDoc loader Jar file...");
			var modsDir = FMLPaths.MODSDIR.get();
			var jarName = "%s-renderdoc-loader.jar".formatted(SparkweaveMod.MODID);
			var jarFile = modsDir.resolve(jarName);
			if(Files.exists(jarFile)) {
				try(var stream = new JarInputStream(Files.newInputStream(jarFile))) {
					var foundVersion = stream.getManifest().getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
					LOGGER.debug("Found existing loader file, version {}", foundVersion);
					if(foundVersion.equals(currentModVersion)) {
						LOGGER.info("Version matches, continuing launch!");
						pb.complete();
						return;
					}
				}
			}
			pb.increment();

			var internalJarName = "META-INF/jarjar/%s-renderdoc-loader-%s.jar".formatted(SparkweaveMod.MODID, currentModVersion);
			var internalJarFile = modContainer.getModInfo().getOwningFile().getFile().findResource(internalJarName);
			if(!Files.exists(internalJarFile)) {
				throw new FileNotFoundException("Unable to find %s in mod resources!".formatted(internalJarName));
			}
			LOGGER.debug("Extracting new file...");
			Files.copy(internalJarFile, jarFile, StandardCopyOption.REPLACE_EXISTING);
			pb.increment();

			pb.label("RenderDoc loader file extracted.");
			// TODO make translatable once NEO fixes this issue
			//noinspection UnstableApiUsage
			ModLoader.addLoadingIssue(new ModLoadingIssue(ModLoadingIssue.Severity.ERROR, "RenderDoc loader successfully extracted, please restart your game!", List.of()));

			StartupNotificationManager.addModMessage("Successfully extracted RenderDoc loader, waiting for restart.");
			pb.complete();

//			throw new ModLoadingException(new ModLoadingIssue(ModLoadingIssue.Severity.ERROR, "RenderDoc loader successfully extracted, please restart your game!", List.of()));
		}
	}

	@SuppressWarnings("unchecked")
	@Nullable
	private static <T> Class<T> getClass(String name) {
		try {
			return (Class<T>) Class.forName(name);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
}
