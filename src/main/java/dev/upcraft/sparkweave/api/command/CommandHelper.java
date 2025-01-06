package dev.upcraft.sparkweave.api.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.upcraft.sparkweave.mixin.ArgumentTypeInfosAccessor;
import dev.upcraft.sparkweave.mixin.CommandSourceStackAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class CommandHelper {

	public static final DynamicCommandExceptionType IO_EXCEPTION = new DynamicCommandExceptionType(ex -> Component.translatable("commands.sparkweave.error.io_exception", ex instanceof Throwable t ? t.getMessage() : ex));

	public static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> createArgumentInfo(Class<A> clazz, ArgumentTypeInfo<A, T> info) {
		ArgumentTypeInfosAccessor.sparkweave$getByClass().put(clazz, info);
		return info;
	}

	/**
	 * Send a {@link Path} to the command source player (if any), and send the {@code baseMessage} to every admin
	 */
	public static void sendPathResult(CommandContext<CommandSourceStack> ctx, Path location, Supplier<Component> baseMessage, UnaryOperator<Component> pathMessage) throws CommandSyntaxException {
		var player = ctx.getSource().getPlayer();
		var server = ctx.getSource().getServer();

		if (player != null && server.isSingleplayerOwner(player.getGameProfile())) {
			var locationString = location.toAbsolutePath().toString();
			var pathComponent = Component.literal(locationString).withStyle(style -> style.applyFormats(ChatFormatting.BLUE, ChatFormatting.UNDERLINE).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.sparkweave.open_folder"))).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, locationString)));
			if(!((CommandSourceStackAccessor) ctx.getSource()).sparkweave$isSilent()) {
				// TODO send custom packet to bypass restriction on OPEN_FILE click event
				player.sendSystemMessage(pathMessage.apply(pathComponent));
			}
			broadcastToAdmins(server, baseMessage.get(), player.getGameProfile().getId());
		}
		else {
			ctx.getSource().sendSystemMessage(baseMessage.get());

			// dont ignore source because source is not a player
			broadcastToAdmins(server, baseMessage.get());
		}
	}

	/**
	 * broadcast a message to server operators
	 * @see CommandHelper#broadcastToAdmins(MinecraftServer, Component, boolean, UUID...)
	 */
	public static void broadcastToAdmins(MinecraftServer server, Component message, UUID... exclude) {
		broadcastToAdmins(server, message, false, exclude);
	}

	/**
	 * broadcast a message to server operators
	 * @param ignoreSendCommandFeedback if {@code true} will ignore the {@code sendCommandFeedback} game rule
	 * @param exclude players to <strong>NOT</strong> send the message to, even if they are an operator
	 */
	public static void broadcastToAdmins(MinecraftServer server, Component message, boolean ignoreSendCommandFeedback, UUID... exclude) {
		var excludeSet = Set.of(exclude);
		if (ignoreSendCommandFeedback || server.getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK)) {
			for (var serverPlayer : server.getPlayerList().getPlayers()) {
				var profile = serverPlayer.getGameProfile();
				if (server.getPlayerList().isOp(serverPlayer.getGameProfile()) && !excludeSet.contains(profile.getId())) {
					serverPlayer.sendSystemMessage(message);
				}
			}
		}
	}
}
