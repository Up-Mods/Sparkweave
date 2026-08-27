package dev.upcraft.sparkweave.platform;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.upcraft.sparkweave.api.command.CommandHelper;
import dev.upcraft.sparkweave.api.item.CreativeTabHelper;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public interface SparkweaveHelperService {

	/// @see CreativeTabHelper
	CreativeModeTab.Builder newCreativeTabBuilder(Component title);

	/// @see CommandHelper#createArgumentInfo(Class, ArgumentTypeInfo)
	<A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> create(Class<A> clazz, ArgumentTypeInfo<A, T> info);
}
