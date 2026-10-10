package dev.upcraft.sparkweave.mixin.datagen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.upcraft.sparkweave.datagen.SparkweaveDatagenHelper;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(BlockLootSubProvider.class)
public class BlockLootSubProviderMixin {

	@WrapOperation(method = "generate(Ljava/util/function/BiConsumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getLootTable()Ljava/util/Optional;"))
	private Optional<ResourceKey<LootTable>> filterOnlyTargetMod(Block instance, Operation<Optional<ResourceKey<LootTable>>> original) {
		var table = original.call(instance);

		if(!SparkweaveDatagenHelper.ENABLED_MODS.isEmpty()) {
			return table.filter(lootTableResourceKey -> SparkweaveDatagenHelper.ENABLED_MODS.contains(lootTableResourceKey.identifier().getNamespace()));
		}

		return table;
	}
}
