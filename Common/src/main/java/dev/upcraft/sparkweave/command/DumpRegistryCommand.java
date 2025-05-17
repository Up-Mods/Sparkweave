package dev.upcraft.sparkweave.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.command.CommandHelper;
import dev.upcraft.sparkweave.api.command.argument.RegistryArgumentType;
import dev.upcraft.sparkweave.api.logging.SparkweaveLoggerFactory;
import dev.upcraft.sparkweave.api.platform.Services;
import dev.upcraft.sparkweave.api.serialization.CSVWriter;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceKey;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DumpRegistryCommand {

	private static final Logger LOGGER = SparkweaveLoggerFactory.getLogger();

	public static void register(LiteralArgumentBuilder<CommandSourceStack> $) {
		$.then(Commands.literal("dump_registries")
			.requires(src -> src.hasPermission(Commands.LEVEL_OWNERS))
			.executes(DumpRegistryCommand::dumpAllRegistries)
			.then(Commands.argument("registry", RegistryArgumentType.registry())
				.executes(ctx -> dumpRegistry(ctx, RegistryArgumentType.getRegistry(ctx, "registry")))
			)
			.then(Commands.literal("all")
				.executes(DumpRegistryCommand::dumpAllRegistries)
			)
		);
	}

	private static int dumpAllRegistries(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		var player = ctx.getSource().getPlayerOrException();
		var registryAccess = ctx.getSource().getServer().registries().compositeAccess();
		var registries = registryAccess.listRegistries().toList();
		var dir = Services.PLATFORM.getGameDir().resolve(SparkweaveMod.MODID).resolve("registry_export");

		for (ResourceKey<? extends Registry<?>> registryKey : registries) {
			var registry = registryAccess.registry(registryKey).orElseThrow();
			saveRegistryToFile(registry, dir);
		}

		if (ctx.getSource().getServer().isSingleplayerOwner(player.getGameProfile())) {
			var path = Component.literal(dir.toString()).withStyle(style -> style
				.applyFormats(ChatFormatting.BLUE, ChatFormatting.UNDERLINE)
				.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.sparkweave.open_folder")))
				.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, dir.toString()))
			);

			//TODO directly send to client to bypass message click event filtering
			ctx.getSource().sendSuccess(() -> Component.translatable("commands.sparkweave.debug.dump_registries.multi_success_path", registries.size(), path), true);
		} else {
			ctx.getSource().sendSuccess(() -> Component.translatable("commands.sparkweave.debug.dump_registries.multi_success", registries.size()), true);
		}

		return registries.size();
	}

	private static int dumpRegistry(CommandContext<CommandSourceStack> ctx, Registry<?> registry) throws CommandSyntaxException {
		var player = ctx.getSource().getPlayerOrException();

		var dir = Services.PLATFORM.getGameDir().resolve("sparkweave").resolve("registry_export");
		saveRegistryToFile(registry, dir);

		if (ctx.getSource().getServer().isSingleplayerOwner(player.getGameProfile())) {
			var path = Component.literal(dir.toString()).withStyle(style -> style
				.applyFormats(ChatFormatting.BLUE, ChatFormatting.UNDERLINE)
				.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.sparkweave.open_folder")))
				.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, dir.toString()))
			);

			//TODO directly send to client to bypass message click event filtering
			ctx.getSource().sendSuccess(() -> Component.translatable("commands.sparkweave.debug.dump_registries.success_path", registry.key().location(), path), true);
		} else {
			ctx.getSource().sendSuccess(() -> Component.translatable("commands.sparkweave.debug.dump_registries.success", registry.key().location()), true);
		}

		return Command.SINGLE_SUCCESS;
	}

	private static void saveRegistryToFile(Registry<?> registry, Path outputDir) throws CommandSyntaxException {
		// TODO logging
		var outputFile = outputDir.resolve(registry.key().location().getNamespace()).resolve(registry.key().location().getPath() + ".csv");
		try {
			Files.createDirectories(outputFile.getParent());
			try (var stream = Files.newOutputStream(outputFile)) {
				try (var writer = CSVWriter.create(stream, "namespace", "path")) {
					registry.keySet().stream().sorted().forEachOrdered(key -> writer.addRow(key.getNamespace(), key.getPath()));
				}
			}
		} catch (IOException e) {
			LOGGER.error("Failed to write registry dump for {}", registry.key().location(), e);
			throw CommandHelper.IO_EXCEPTION.create(e.getMessage());
		}
	}
}
