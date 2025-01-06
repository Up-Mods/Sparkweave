package dev.upcraft.sparkweave.command;

import com.mojang.brigadier.CommandDispatcher;
import dev.upcraft.sparkweave.SparkweaveHelper;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class SparkweaveCommandRoot {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection environment) {
		var root = Commands.literal(SparkweaveHelper.MODID);
		var debug = Commands.literal("debug").requires(src -> src.hasPermission(Commands.LEVEL_ADMINS));

		DumpRecipesCommand.register(debug, buildContext);
		DumpRegistryCommand.register(debug);
		DumpTagsCommand.register(debug);

		root.then(debug);
		dispatcher.register(root);
	}
}
