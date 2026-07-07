package dev.upcraft.sparkweave.neoforge.mixin.client.command;

import dev.upcraft.sparkweave.api.client.command.ClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.ClientCommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Mixin(ClientCommandSourceStack.class)
public abstract class ClientCommandSourceStackMixin implements ClientCommandSource {

	@Shadow
	public abstract Level getUnsidedLevel();

	@Shadow
	public abstract void sendSuccess(Supplier<Component> message, boolean sendToAdmins);

	@Override
	public void sendFeedback(Component message) {
		this.sendSuccess(() -> message, false);
	}

	@Override
	public Minecraft getClient() {
		return Minecraft.getInstance();
	}

	@Override
	public ClientLevel getClientLevel() {
		return (ClientLevel) getUnsidedLevel();
	}

	@Override
	public LocalPlayer getClientPlayer() {
		return getClient().player;
	}
}
