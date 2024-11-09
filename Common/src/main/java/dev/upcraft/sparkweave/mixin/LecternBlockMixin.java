package dev.upcraft.sparkweave.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.upcraft.sparkweave.event.LecternMenuRegistry;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.OptionalInt;

@Mixin(LecternBlock.class)
public class LecternBlockMixin {

	@WrapOperation(method = "openScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;openMenu(Lnet/minecraft/world/MenuProvider;)Ljava/util/OptionalInt;"))
	private OptionalInt customMenuProvider(Player instance, MenuProvider menu, Operation<OptionalInt> original) {
		if (menu instanceof LecternBlockEntity lectern) {
			var provider = LecternMenuRegistry.get(lectern.getBook()).map(factory -> sparkweave$createProvider(factory, instance, lectern)).orElse(menu);
			return original.call(instance, provider);
		}

		return original.call(instance, menu);
	}

	@Unique
	@Nullable
	private static MenuProvider sparkweave$createProvider(LecternMenuRegistry.MenuProviderFactory factory, Player player, LecternBlockEntity lectern) {
		return factory.create(player.level(), lectern.getBlockPos(), player, lectern, lectern.getBook());
	}
}
