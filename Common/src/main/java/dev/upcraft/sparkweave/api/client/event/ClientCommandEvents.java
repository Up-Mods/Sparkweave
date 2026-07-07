package dev.upcraft.sparkweave.api.client.event;

import com.mojang.brigadier.CommandDispatcher;
import dev.upcraft.sparkweave.api.client.command.ClientCommandSource;
import dev.upcraft.sparkweave.api.event.Event;
import net.minecraft.commands.CommandBuildContext;

public class ClientCommandEvents {

	public static final Event<Register> REGISTER = Event.create(Register.class, listeners -> (dispatcher, buildContext) -> {
		for (var listener : listeners) {
			listener.registerClientCommands(dispatcher, buildContext);
		}
	});

	@FunctionalInterface
	public interface Register {

		void registerClientCommands(CommandDispatcher<ClientCommandSource> dispatcher, CommandBuildContext buildContext);
	}
}
