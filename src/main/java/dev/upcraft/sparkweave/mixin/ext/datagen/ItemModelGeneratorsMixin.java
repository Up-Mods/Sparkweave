package dev.upcraft.sparkweave.mixin.ext.datagen;

import dev.upcraft.sparkweave.api.util.ext.datagen.ItemModelGeneratorsExt;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ItemModelGenerators.class)
public abstract class ItemModelGeneratorsMixin implements ItemModelGeneratorsExt {

	@Shadow
	public abstract void generateFlatItem(Item item, ModelTemplate modelTemplate);

	@Override
	public void createSpawnEgg(Supplier<? extends ItemLike> item) {
		generateFlatItem(item.get().asItem(), ItemModelGeneratorsExt.SPAWN_EGG_TEMPLATE);
	}
}
