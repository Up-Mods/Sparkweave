package dev.upcraft.sparkweave.api.datagen;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Util;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.function.Supplier;

public class TranslationBuilder {

	private final HolderLookup.Provider registries;
	private final ImmutableMap.Builder<String, String> mapBuilder = new ImmutableMap.Builder<>();

	public TranslationBuilder(HolderLookup.Provider registries) {
		this.registries = registries;
	}

	public void add(String key, String translation) {
		Preconditions.checkNotNull(translation, "No translation provided for " + key);
		mapBuilder.put(key, translation);
	}

	public void attribute(Supplier<? extends Attribute> attribute, String translation) {
		add(attribute.get().getDescriptionId(), translation);
	}

	public void block(Supplier<? extends Block> block, String translation) {
		add(block.get().getDescriptionId(), translation);
	}

	public void creativeTab(RegistrySupplier<CreativeModeTab> tab, String translation) {
		add(Util.makeDescriptionId("itemGroup", tab.getId()), translation);
	}

	public void entity(Supplier<? extends EntityType<? extends Entity>> entity, String translation) {
		add(entity.get().getDescriptionId(), translation);
	}

	public void item(Supplier<? extends Item> item, String translation) {
		add(item.get().getDescriptionId(), translation);
	}

	public void mobEffect(Supplier<? extends MobEffect> effect, String translation) {
		add(effect.get().getDescriptionId(), translation);
	}

	/**
	 * should not be used in en_us file
	 */
	@ApiStatus.Experimental
	public void tag(TagKey<?> tagKey, String translation) {
		add(tagKey.getDescriptionId(), translation);
	}

	@ApiStatus.Internal
	public Map<String, String> build() {
		return mapBuilder.buildOrThrow();
	}
}
