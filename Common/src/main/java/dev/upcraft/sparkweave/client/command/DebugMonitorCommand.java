package dev.upcraft.sparkweave.client.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.upcraft.sparkweave.api.client.command.ClientCommandSource;
import dev.upcraft.sparkweave.api.client.command.ClientCommands;

public class DebugMonitorCommand {

	public static void register(LiteralArgumentBuilder<ClientCommandSource> root) {
		root.then(ClientCommands.literal("debug")
			.then(ClientCommands.literal("monitor").executes(context -> {
			return 0;
		})));
	}
}
