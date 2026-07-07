package dev.upcraft.sparkweave.client;

import com.google.auto.service.AutoService;
import dev.upcraft.sparkweave.api.client.event.ClientCommandEvents;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.client.command.ClientRootCommand;

@AutoService(ClientEntryPoint.class)
public class SparkweaveModClient implements ClientEntryPoint {

	@Override
	public void onInitializeClient(ModContainer mod) {
		ClientCommandEvents.REGISTER.register(ClientRootCommand::register);
	}
}
