package dev.upcraft.sparkweave.fabric.service;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.api.platform.services.SparkweaveHelperService;
import dev.upcraft.sparkweave.fabric.mixin.ArgumentTypeInfosAccessor;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public class FabricHelperService implements SparkweaveHelperService {

	@CalledByReflection
	public FabricHelperService() {
		// need an explicit default constructor for the service loader to work
	}

	@Override
	public CreativeModeTab.Builder newCreativeTabBuilder(Component title) {
		return FabricItemGroup.builder().title(title);
	}

	@Override
	public synchronized <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> create(Class<A> clazz, ArgumentTypeInfo<A, T> info) {
		ArgumentTypeInfosAccessor.sparkweave$getByClass().put(clazz, info);
		return info;
	}
}
