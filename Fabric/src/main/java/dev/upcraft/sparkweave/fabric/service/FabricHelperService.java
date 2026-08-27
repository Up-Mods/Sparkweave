package dev.upcraft.sparkweave.fabric.service;

import com.google.auto.service.AutoService;
import com.mojang.brigadier.arguments.ArgumentType;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import dev.upcraft.sparkweave.fabric.mixin.ArgumentTypeInfosAccessor;
import dev.upcraft.sparkweave.platform.SparkweaveHelperService;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

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

	@Override
	public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityType.BlockEntitySupplier<T> factory, boolean onlyOpCanSetNbt, Block[] validBlocks) {
		return FabricBlockEntityTypeBuilder.create(factory::create, validBlocks).canPotentiallyExecuteCommands(onlyOpCanSetNbt).build();
	}
}
