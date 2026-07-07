package dev.upcraft.sparkweave.fabric.service;

import com.google.auto.service.AutoService;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.api.platform.services.SparkweaveHelperService;
import dev.upcraft.sparkweave.fabric.mixin.ArgumentTypeInfosAccessor;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

@AutoService(SparkweaveHelperService.class)
public class FabricHelperService implements SparkweaveHelperService {

	@CalledByReflection
	public FabricHelperService() {
		// need an explicit default constructor for the service loader to work
	}

	@Override
	public CreativeModeTab.Builder newCreativeTabBuilder(Component title) {
		return FabricCreativeModeTab.builder().title(title);
	}

	@Override
	public synchronized <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> create(Class<A> clazz, ArgumentTypeInfo<A, T> info) {
		ArgumentTypeInfosAccessor.sparkweave$getByClass().put(clazz, info);
		return info;
	}
}
