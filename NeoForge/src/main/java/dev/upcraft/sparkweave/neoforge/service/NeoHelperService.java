package dev.upcraft.sparkweave.neoforge.service;

import com.google.auto.service.AutoService;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.platform.SparkweaveHelperService;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

@AutoService(SparkweaveHelperService.class)
public class NeoHelperService implements SparkweaveHelperService {

	@CalledByReflection
	public NeoHelperService() {
		// need an explicit default constructor for the service loader to work
	}

	@Override
	public CreativeModeTab.Builder newCreativeTabBuilder(Component title) {
		return CreativeModeTab.builder().title(title);
	}

	@Override
	public <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> create(Class<A> clazz, ArgumentTypeInfo<A, T> info) {
		return ArgumentTypeInfos.registerByClass(clazz, info);
	}
}
