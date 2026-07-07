package dev.upcraft.sparkweave.fabric.mixin.client.command;

import dev.upcraft.sparkweave.api.client.command.ClientCommandSource;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientSuggestionProvider.class)
public abstract class ClientSuggestionProviderMixin implements ClientCommandSource, FabricClientCommandSource {

	@Override
	public void sendFailure(Component message) {
		sendError(message);
	}

	@Override
	public ClientLevel getClientLevel() {
		return getLevel();
	}

	@Override
	public LocalPlayer getClientPlayer() {
		return getPlayer();
	}
}
