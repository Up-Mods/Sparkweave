package dev.upcraft.sparkweave.api.client.ext.datagen;

import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;

public interface ItemModelGeneratorsExt {

	default ItemModelOutput getItemModelOutput() {
		throw new AssertionError("Implemented in Mixin");
	}

	default BiConsumer<Identifier, ModelInstance> getModelOutput() {
		throw new AssertionError("Implemented in Mixin");
	}
}
