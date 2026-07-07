package dev.upcraft.sparkweave.platform;

import dev.upcraft.sparkweave.SparkweaveMod;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;

public abstract class BasePlatformService {

	protected final AppDirs appDirs = AppDirsFactory.getInstance();

	public Path getUserDataDir(@Nullable String subDirectory) {
		Path basePath = Path.of(appDirs.getUserDataDir("." + SparkweaveMod.MODID, null, null, false));

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
