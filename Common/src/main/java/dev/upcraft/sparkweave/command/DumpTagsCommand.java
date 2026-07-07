package dev.upcraft.sparkweave.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.command.CommandHelper;
import dev.upcraft.sparkweave.api.command.argument.RegistryArgumentType;
import dev.upcraft.sparkweave.api.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceKey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DumpTagsCommand {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static void register(LiteralArgumentBuilder<CommandSourceStack> $) {
		$.then(Commands.literal("dump_tags")
			.requires(src -> Commands.LEVEL_OWNERS.check(src.permissions()))
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
		var registries = registryAccess.listRegistryKeys().toList();
		var dir = Services.PLATFORM.getGameDir().resolve(SparkweaveMod.MODID).resolve("tag_export");

		for (ResourceKey<? extends Registry<?>> registryKey : registries) {
			var registry = registryAccess.lookupOrThrow(registryKey);
			saveTags(registry, dir);
		}

		if (ctx.getSource().getServer().isSingleplayerOwner(player.nameAndId())) {
			var path = Component.literal(dir.toString()).withStyle(style -> style
				.applyFormats(ChatFormatting.BLUE, ChatFormatting.UNDERLINE)
				.withHoverEvent(new HoverEvent.ShowText(Component.translatable("chat.sparkweave.open_folder")))
				.withClickEvent(new ClickEvent.OpenFile(dir.toString()))
			);

			//TODO directly send to client to bypass message click event filtering
			ctx.getSource().sendSuccess(() -> Component.translatable("commands.sparkweave.debug.dump_tags.multi_success_path", registries.size(), path), true);
		} else {
			ctx.getSource().sendSuccess(() -> Component.translatable("commands.sparkweave.debug.dump_tags.multi_success", registries.size()), true);
		}

		return registries.size();
	}

	private static int dumpTags(CommandContext<CommandSourceStack> ctx, Registry<?> registry) throws CommandSyntaxException {
		var dir = Services.PLATFORM.getGameDir().resolve(SparkweaveMod.MODID).resolve("tag_export");
		saveTags(registry, dir);
		CommandHelper.sendPathResult(ctx, dir.resolve(registry.key().identifier().getNamespace()).resolve(registry.key().identifier().getPath()), () -> Component.translatable("commands.sparkweave.debug.dump_tags.success", registry.key().identifier()), path -> Component.translatable("commands.sparkweave.debug.dump_tags.success_path", registry.key().identifier(), path));
		return Command.SINGLE_SUCCESS;
	}

	private static void saveTags(Registry<?> registry, Path dir) throws CommandSyntaxException {
		Path rootDir = dir.resolve(registry.key().identifier().getNamespace()).resolve(registry.key().identifier().getPath());

		var tags = registry.getTags().toList();
		for (var tagPair : tags) {
			var name = tagPair.key().location();
			var outputFile = rootDir.resolve(name.getNamespace()).resolve(name.getPath() + ".json");

			var json = new JsonObject();
			var array = new JsonArray();
			tagPair.stream()
				.map(holder -> holder.unwrap().map(k -> k.identifier().toString(), Object::toString))
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
