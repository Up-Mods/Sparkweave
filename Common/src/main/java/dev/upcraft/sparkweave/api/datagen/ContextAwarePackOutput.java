package dev.upcraft.sparkweave.api.datagen;

import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;

public class ContextAwarePackOutput extends PackOutput {

	private final ModContainer modContainer;

	public ContextAwarePackOutput(Path outputFolder, ModContainer modContainer) {
		super(outputFolder);
		this.modContainer = modContainer;
	}

	public ModContainer getModContainer() {
		return modContainer;
	}
}
