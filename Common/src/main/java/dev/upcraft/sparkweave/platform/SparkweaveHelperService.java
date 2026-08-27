package dev.upcraft.sparkweave.platform;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.upcraft.sparkweave.api.command.CommandHelper;
import dev.upcraft.sparkweave.api.item.CreativeTabHelper;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface SparkweaveHelperService {

	/// @see CreativeTabHelper
	CreativeModeTab.Builder newCreativeTabBuilder(Component title);

	/// @see CommandHelper#createArgumentInfo(Class, ArgumentTypeInfo)
	<A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> create(Class<A> clazz, ArgumentTypeInfo<A, T> info);

	<T extends BlockEntity>BlockEntityType<T> createBlockEntityType(BlockEntityType.BlockEntitySupplier<T> factory, boolean onlyOpCanSetNbt, Block[] validBlocks);
}
