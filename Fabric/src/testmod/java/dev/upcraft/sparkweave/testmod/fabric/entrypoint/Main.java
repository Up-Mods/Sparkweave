package dev.upcraft.sparkweave.testmod.fabric.entrypoint;

import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import net.fabricmc.api.ModInitializer;

@CalledByReflection
public class Main implements ModInitializer {

	@Override
	public void onInitialize() {
		SparkweaveTestmod.init();
	}
}
