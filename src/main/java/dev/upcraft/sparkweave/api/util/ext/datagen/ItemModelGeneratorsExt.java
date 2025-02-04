package dev.upcraft.sparkweave.api.util.ext.datagen;

import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public interface ItemModelGeneratorsExt {

	ModelTemplate SPAWN_EGG_TEMPLATE = ModelTemplates.createItem("template_spawn_egg");

	default void createSpawnEgg(Supplier<? extends ItemLike> item) {
		throw new UnsupportedOperationException();
	}
}
