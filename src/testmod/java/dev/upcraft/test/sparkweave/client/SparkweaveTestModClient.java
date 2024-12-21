package dev.upcraft.test.sparkweave.client;

import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.api.client.Debug;
import dev.upcraft.sparkweave.api.util.Time;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

@CalledByReflection
public class SparkweaveTestModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientTickEvents.START_CLIENT_TICK.register(new ClientTickEvents.StartTick() {
			int ticks = 0;
			@Override
			public void onStartTick(Minecraft client) {
				if (client.level != null && ticks++ % 100 == 0) {
					for (int x = 0; x < 16; x++) {
						for (int z = 0; z < 16; z++) {
							Debug.setColor(50 + x * 10, 0, 135 + z * 8);
							Debug.drawLine(x, 0, z, x, 1, z, Time.toMillis(100));
						}
					}

				}
			}
		});
	}
}
