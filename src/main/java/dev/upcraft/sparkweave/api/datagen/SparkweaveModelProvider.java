package dev.upcraft.sparkweave.api.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.world.level.block.Block;

public abstract class SparkweaveModelProvider extends FabricModelProvider {

	public SparkweaveModelProvider(FabricDataOutput output) {
		super(output);
	}

	public void copyModelNoItem(BlockModelGenerators gen, Block sourceBlock, Block targetBlock) {
		gen.blockStateOutput.accept(MultiVariantGenerator.multiVariant(targetBlock, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(sourceBlock))));
	}
}
