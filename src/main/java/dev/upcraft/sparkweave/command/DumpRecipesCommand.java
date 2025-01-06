package dev.upcraft.sparkweave.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.upcraft.sparkweave.SparkweaveHelper;
import dev.upcraft.sparkweave.api.Platform;
import dev.upcraft.sparkweave.api.command.CommandHelper;
import dev.upcraft.sparkweave.api.serialization.CSVWriter;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DumpRecipesCommand {

	private static final DynamicCommandExceptionType TYPE_NOT_FOUND = new DynamicCommandExceptionType(type -> Component.translatable("argument.sparkweave.debug.dump_recipes.type_not_found", type));

	public static void register(LiteralArgumentBuilder<CommandSourceStack> $, CommandBuildContext buildContext) {
		$.then(Commands.literal("dump_recipes")
			.requires(src -> src.hasPermission(Commands.LEVEL_OWNERS))
			.executes(DumpRecipesCommand::dumpAllRecipes)
			.then(Commands.argument("type", ResourceArgument.resource(buildContext, Registries.RECIPE_TYPE))
				.executes(ctx -> dumpRecipes(ctx, ResourceArgument.getResource(ctx, "type", Registries.RECIPE_TYPE)))
			)
			.then(Commands.literal("all")
				.executes(DumpRecipesCommand::dumpAllRecipes)
			)
		);
	}

	private static int dumpRecipes(CommandContext<CommandSourceStack> ctx, Holder.Reference<RecipeType<?>> type) throws CommandSyntaxException {
		var dir = Platform.getGameDir().resolve(SparkweaveHelper.MODID).resolve("recipe_export");

		saveRecipes(ctx, type, dir);
		CommandHelper.sendPathResult(ctx, dir.resolve(type.key().location().getNamespace()).resolve(type.key().location().getPath()), () -> Component.translatable("commands.sparkweave.debug.dump_recipes.success", type.key().location()), path -> Component.translatable("commands.sparkweave.debug.dump_recipes.success_path", type.key().location(), path));
		return Command.SINGLE_SUCCESS;
	}

	private static int dumpAllRecipes(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		var player = ctx.getSource().getPlayerOrException();
		var dir = Platform.getGameDir().resolve(SparkweaveHelper.MODID).resolve("recipe_export");

		var types = ctx.getSource().registryAccess().registryOrThrow(Registries.RECIPE_TYPE).holders().toList();
		for (var type : types) {
			saveRecipes(ctx, type, dir);
		}

		if (ctx.getSource().getServer().isSingleplayerOwner(player.getGameProfile())) {
			var path = Component.literal(dir.toString()).withStyle(style -> style
				.applyFormats(ChatFormatting.BLUE, ChatFormatting.UNDERLINE)
				.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.sparkweave.open_folder")))
				.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, dir.toString()))
			);

			//TODO directly send to client to bypass message click event filtering
			ctx.getSource().sendSuccess(() -> Component.translatable("commands.sparkweave.debug.dump_recipes.multi_success_path", types.size(), path), true);
		} else {
			ctx.getSource().sendSuccess(() -> Component.translatable("commands.sparkweave.debug.dump_recipes.multi_success", types.size()), true);
		}

		return types.size();
	}

	private static void saveRecipes(CommandContext<CommandSourceStack> ctx, Holder.Reference<RecipeType<?>> holder, Path dir) throws CommandSyntaxException {
		if (!holder.isBound()) {
			throw TYPE_NOT_FOUND.create(holder.key().location());
		}

		//noinspection unchecked
		var recipes = ctx.getSource().getServer().getRecipeManager().getAllRecipesFor((RecipeType<Recipe<Container>>) holder.value());
		var outputFile = dir.resolve(holder.key().location().getNamespace()).resolve(holder.key().location().getPath() + ".csv");
		var serializers = ctx.getSource().registryAccess().registryOrThrow(Registries.RECIPE_SERIALIZER);
		try {
			Files.createDirectories(outputFile.getParent());
			try (var writer = CSVWriter.create(Files.newOutputStream(outputFile), "namespace", "path", "group", "serializer", "special")) {
				for (var recipe : recipes) {
					writer.addRow(recipe.getId().getNamespace(), recipe.getId().getPath(), recipe.getGroup(), serializers.getKey(recipe.getSerializer()), recipe.isSpecial());
				}
			}
		} catch (IOException e) {
			throw CommandHelper.IO_EXCEPTION.create(e);
		}
	}
}
