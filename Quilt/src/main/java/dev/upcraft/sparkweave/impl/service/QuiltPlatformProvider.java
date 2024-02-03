package dev.upcraft.sparkweave.impl.service;

import dev.upcraft.sparkweave.SparkweaveHelper;
import dev.upcraft.sparkweave.api.PlatformProvider;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.impl.MacOSXAppDirs;
import net.harawata.appdirs.impl.ShellFolderResolver;
import net.harawata.appdirs.impl.UnixAppDirs;
import net.harawata.appdirs.impl.WindowsAppDirs;
import net.minecraft.Util;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.QuiltLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;

@CalledByReflection
public class QuiltPlatformProvider implements PlatformProvider {

	private final AppDirs appDirs;

	public QuiltPlatformProvider() {
		switch (Util.getPlatform()) {
			case WINDOWS -> appDirs = new WindowsAppDirs(new ShellFolderResolver());
			case LINUX -> appDirs = new UnixAppDirs();
			case OSX -> appDirs = new MacOSXAppDirs();
			default -> throw new IllegalStateException("Unsupported OS: " + System.getProperty("os.name"));
		}
	}

	@Override
	public boolean isModLoaded(String modid) {
		return QuiltLoader.isModLoaded(modid);
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return QuiltLoader.isDevelopmentEnvironment();
	}

	@Override
	public Path getGameDir() {
		return QuiltLoader.getGameDir();
	}

	@Override
	public Path getConfigDir() {
		return QuiltLoader.getConfigDir();
	}

	@Override
	public Path getUserDataDir(@Nullable String subDirectory) {

		Path homeDir = Path.of(appDirs.getUserDataDir(SparkweaveHelper.MODID, null, null, false));
		Path basePath = homeDir.resolve("." + SparkweaveHelper.MODID);

		try {
			Files.createDirectories(basePath);
			if (Util.getPlatform() == Util.OS.WINDOWS) {
				DosFileAttributeView attributes = Files.getFileAttributeView(basePath, DosFileAttributeView.class);
				if (!attributes.readAttributes().isHidden()) {
					attributes.setHidden(true);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("unable to create or modify directory at " + basePath.toAbsolutePath(), e);
		}
		return subDirectory != null ? basePath.resolve(subDirectory) : basePath;
	}

}
