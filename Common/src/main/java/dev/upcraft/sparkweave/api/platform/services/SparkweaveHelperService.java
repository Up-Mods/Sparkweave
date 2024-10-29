package dev.upcraft.sparkweave.api.platform.services;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public interface SparkweaveHelperService {

	/**
	 * @see dev.upcraft.sparkweave.api.item.CreativeTabHelper
	 */
	CreativeModeTab.Builder newCreativeTabBuilder(Component title);

	/**
	 * @see dev.upcraft.sparkweave.api.command.CommandHelper#createArgumentInfo(Class, ArgumentTypeInfo)
	 */
	<A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> create(Class<A> clazz, ArgumentTypeInfo<A, T> info);
}
