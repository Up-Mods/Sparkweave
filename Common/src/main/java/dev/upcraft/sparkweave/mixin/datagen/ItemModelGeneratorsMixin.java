package dev.upcraft.sparkweave.mixin.datagen;

import dev.upcraft.sparkweave.api.client.ext.datagen.ItemModelGeneratorsExt;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.BiConsumer;

@Mixin(ItemModelGenerators.class)
public abstract class ItemModelGeneratorsMixin implements ItemModelGeneratorsExt {

	@Accessor("itemModelOutput")
	@Override
	public abstract ItemModelOutput getItemModelOutput();

	@Accessor("modelOutput")
	@Override
	public abstract BiConsumer<Identifier, ModelInstance> getModelOutput();
}
