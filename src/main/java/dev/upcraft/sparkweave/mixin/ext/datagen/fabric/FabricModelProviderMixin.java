package dev.upcraft.sparkweave.mixin.ext.datagen.fabric;

import dev.upcraft.sparkweave.api.util.ext.datagen.fabric.FabricModelProviderExt;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(FabricModelProvider.class)
public abstract class FabricModelProviderMixin implements FabricModelProviderExt {

	@Override
	public void copyModelNoItem(BlockModelGenerators gen, Block sourceBlock, Block targetBlock) {
		gen.blockStateOutput.accept(MultiVariantGenerator.multiVariant(targetBlock, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(sourceBlock))));
	}
}
