package dev.upcraft.sparkweave.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.upcraft.sparkweave.SparkweaveHelper;
import dev.upcraft.sparkweave.api.Platform;
import dev.upcraft.sparkweave.api.command.CommandHelper;
import dev.upcraft.sparkweave.api.command.argument.RegistryArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DumpTagsCommand {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static void register(LiteralArgumentBuilder<CommandSourceStack> $) {
		$.then(Commands.literal("dump_tags")
			.requires(src -> src.hasPermission(Commands.LEVEL_OWNERS))
			.executes(DumpTagsCommand::dumpAllTags)
			.then(Commands.argument("type", RegistryArgumentType.registry())
				.executes(ctx -> dumpTags(ctx, RegistryArgumentType.getRegistry(ctx, "type")))
			)
			.then(Commands.literal("all")
				.executes(DumpTagsCommand::dumpAllTags)
			)
		);
	}

	private static int dumpAllTags(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		var player = ctx.getSource().getPlayerOrException();
		var registryAccess = ctx.getSource().getServer().registries().compositeAccess();
		var registries = registryAccess.registries().toList();
		var dir = Platform.getGameDir().resolve(SparkweaveHelper.MODID).resolve("tag_export");

		for (var registry : registries) {
			saveTags(registry.value(), dir);
		}

		if (ctx.getSource().getServer().isSingleplayerOwner(player.getGameProfile())) {
			var path = Component.literal(dir.toString()).withStyle(style -> style
				.applyFormats(ChatFormatting.BLUE, ChatFormatting.UNDERLINE)
				.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.sparkweave.open_folder")))
				.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, dir.toString()))
			);

			//TODO directly send to client to bypass message click event filtering
			ctx.getSource().sendSuccess(() -> Component.translatable("commands.sparkweave.debug.dump_tags.multi_success_path", registries.size(), path), true);
		} else {
			ctx.getSource().sendSuccess(() -> Component.translatable("commands.sparkweave.debug.dump_tags.multi_success", registries.size()), true);
		}

		return registries.size();
	}

	private static int dumpTags(CommandContext<CommandSourceStack> ctx, Registry<?> registry) throws CommandSyntaxException {
		var dir = Platform.getGameDir().resolve(SparkweaveHelper.MODID).resolve("tag_export");
		saveTags(registry, dir);
		CommandHelper.sendPathResult(ctx, dir.resolve(registry.key().location().getNamespace()).resolve(registry.key().location().getPath()), () -> Component.translatable("commands.sparkweave.debug.dump_tags.success", registry.key().location()), path -> Component.translatable("commands.sparkweave.debug.dump_tags.success_path", registry.key().location(), path));
		return Command.SINGLE_SUCCESS;
	}

	private static void saveTags(Registry<?> registry, Path dir) throws CommandSyntaxException {
		Path rootDir = dir.resolve(registry.key().location().getNamespace()).resolve(registry.key().location().getPath());

		var tags = registry.getTags().toList();
		for (var tagPair : tags) {
			var name = tagPair.getFirst().location();
			var outputFile = rootDir.resolve(name.getNamespace()).resolve(name.getPath() + ".json");

			var json = new JsonObject();
			var array = new JsonArray();
			tagPair.getSecond().stream()
				.map(holder -> holder.unwrap().map(k -> k.location().toString(), Object::toString))
				.forEach(array::add);
			json.add("values", array);

			try {
				Files.createDirectories(outputFile.getParent());
				try (var writer = Files.newBufferedWriter(outputFile)) {
					GSON.toJson(json, writer);
				}
			} catch (IOException e) {
				throw CommandHelper.IO_EXCEPTION.create(e);
			}
		}
	}
}
