package dev.upcraft.sparkweave.mixin;

import dev.upcraft.sparkweave.api.ext.BlockPropertiesExt;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "AddedMixinMembersNamePattern"})
@Mixin(BlockBehaviour.Properties.class)
public abstract class BlockPropertiesMixin implements BlockPropertiesExt {
	@Shadow
	public abstract BlockBehaviour.Properties overrideLootTable(Optional<ResourceKey<LootTable>> table);

	@Override
	public BlockBehaviour.Properties setOverridesFrom(Supplier<? extends Block> parent) {
		return this.overrideLootTable(parent.get().getLootTable()).overrideDescription(parent.get().getDescriptionId());
	}
}
