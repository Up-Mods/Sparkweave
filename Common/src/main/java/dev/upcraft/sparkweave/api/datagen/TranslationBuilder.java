package dev.upcraft.sparkweave.api.datagen;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
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

	public void block(Supplier<? extends Block> block, String name) {
		add(block.get().getDescriptionId(), name);
	}

	public void creativeTab(RegistrySupplier<CreativeModeTab> tab, String name) {
		add(Util.makeDescriptionId("itemGroup", tab.getId()), name);
	}

	public void item(Supplier<? extends Item> item, String name) {
		add(item.get().getDescriptionId(), name);
	}

	@ApiStatus.Internal
	public Map<String, String> build() {
		return mapBuilder.buildOrThrow();
	}
}
