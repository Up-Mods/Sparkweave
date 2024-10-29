package dev.upcraft.sparkweave.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.command.CommandHelper;
import dev.upcraft.sparkweave.api.platform.Services;
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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

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
		var dir = Services.PLATFORM.getGameDir().resolve(SparkweaveMod.MODID).resolve("recipe_export");

		saveRecipes(ctx, type, dir);
		CommandHelper.sendPathResult(ctx, dir.resolve(type.key().location().getNamespace()).resolve(type.key().location().getPath()), () -> Component.translatable("commands.sparkweave.debug.dump_recipes.success", type.key().location()), path -> Component.translatable("commands.sparkweave.debug.dump_recipes.success_path", type.key().location(), path));
		return Command.SINGLE_SUCCESS;
	}

	private static int dumpAllRecipes(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		var player = ctx.getSource().getPlayerOrException();
		var dir = Services.PLATFORM.getGameDir().resolve(SparkweaveMod.MODID).resolve("recipe_export");

		var registry = ctx.getSource().registryAccess().lookupOrThrow(Registries.RECIPE_TYPE).asHolderIdMap();
		for (var type : registry) {
			saveRecipes(ctx, type, dir);
		}

		if (ctx.getSource().getServer().isSingleplayerOwner(player.getGameProfile())) {
			var path = Component.literal(dir.toString()).withStyle(style -> style
				.applyFormats(ChatFormatting.BLUE, ChatFormatting.UNDERLINE)
				.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.sparkweave.open_folder")))
				.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, dir.toString()))
			);

			//TODO directly send to client to bypass message click event filtering
			ctx.getSource().sendSuccess(() -> Component.translatable("commands.sparkweave.debug.dump_recipes.multi_success_path", registry.size(), path), true);
		} else {
			ctx.getSource().sendSuccess(() -> Component.translatable("commands.sparkweave.debug.dump_recipes.multi_success", registry.size()), true);
		}

		return registry.size();
	}

	private static void saveRecipes(CommandContext<CommandSourceStack> ctx, Holder<RecipeType<?>> holder, Path dir) throws CommandSyntaxException {
		var keyOpt = holder.unwrapKey().map(ResourceKey::location);
		if (!holder.isBound() || keyOpt.isEmpty()) {
			throw TYPE_NOT_FOUND.create(keyOpt.orElseGet(() -> ResourceLocation.withDefaultNamespace("unregistered_sadface")));
		}
		var key = keyOpt.orElseThrow();


		var outputFile = dir.resolve(key.getNamespace()).resolve(key.getPath() + ".csv");
		var serializers = ctx.getSource().registryAccess().lookupOrThrow(Registries.RECIPE_SERIALIZER);
		try {
			Files.createDirectories(outputFile.getParent());
			try (var writer = CSVWriter.create(Files.newOutputStream(outputFile), "namespace", "path", "group", "serializer", "special")) {
				ctx.getSource().getServer().getRecipeManager().getRecipes().stream().filter(h -> h.value().getType() == holder.value()).sorted(Comparator.comparing(h -> h.id().location())).forEachOrdered(recipeHolder -> {
					var id = recipeHolder.id().location();
					writer.addRow(id.getNamespace(), id.getPath(), recipeHolder.value().group(), serializers.getKey(recipeHolder.value().getSerializer()), recipeHolder.value().isSpecial());
				});
			}
		} catch (IOException e) {
			throw CommandHelper.IO_EXCEPTION.create(e);
		}
	}
}
