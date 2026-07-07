package dev.upcraft.sparkweave.api.client.command;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public interface ClientCommandSource extends SharedSuggestionProvider {

	void sendFeedback(Component message);

	void sendFailure(Component message);

	Minecraft getClient();

	ClientLevel getClientLevel();

	LocalPlayer getClientPlayer();
}
