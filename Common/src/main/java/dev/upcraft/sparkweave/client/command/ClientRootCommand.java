package dev.upcraft.sparkweave.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.client.command.ClientCommandSource;
import dev.upcraft.sparkweave.api.client.command.ClientCommands;
import dev.upcraft.sparkweave.client.consent.ConsentScreen;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.resources.Identifier;

import java.util.List;

public class ClientRootCommand {

	public static void register(CommandDispatcher<ClientCommandSource> dispatcher, CommandBuildContext buildContext) {
		var root = ClientCommands.literal(SparkweaveMod.MODID);
		DebugMonitorCommand.register(root);

//		//TODO move to testmod
//		root.then(ClientCommands.literal("test")
//			.executes(ClientRootCommand::openConsentScreen));

		dispatcher.register(root);
	}

	private static int openConsentScreen(CommandContext<ClientCommandSource> ctx) {
		List<Identifier> permissions = List.of(
			Identifier.fromNamespaceAndPath("sparkweave", "test1"),
			Identifier.fromNamespaceAndPath("sparkweave", "test2"),
			Identifier.fromNamespaceAndPath("sparkweave", "test3"),
			Identifier.fromNamespaceAndPath("sparkweave", "test4"),
			Identifier.fromNamespaceAndPath("sparkweave", "test5")
		);
		System.out.println("opening screen");
		ctx.getSource().getClient().setScreen(new ConsentScreen(permissions, true));

		return Command.SINGLE_SUCCESS;
	}

}
