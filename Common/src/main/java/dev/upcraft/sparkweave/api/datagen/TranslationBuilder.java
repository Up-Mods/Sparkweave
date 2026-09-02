package dev.upcraft.sparkweave.api.datagen;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.packs.VanillaAdventureAdvancements;
import net.minecraft.data.advancements.packs.VanillaStoryAdvancements;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Util;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

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

	@Deprecated(forRemoval = true)
	public void advancement(Identifier advancementId, String title, String description) {
		add(Util.makeDescriptionId("advancements", advancementId.withSuffix(".title")), title);
		add(Util.makeDescriptionId("advancements", advancementId.withSuffix(".description")), description);
	}

	public void attribute(Supplier<? extends Attribute> attribute, String translation) {
		add(attribute.get().getDescriptionId(), translation);
	}

	@ApiStatus.Experimental
	public void biome(ResourceKey<Biome> biome, String translation) {
		add(Util.makeDescriptionId("biome", biome.identifier()), translation);
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

	public void item(Supplier<? extends ItemLike> item, String translation) {
		add(item.get().asItem().getDescriptionId(), translation);
	}

	@ApiStatus.Experimental
	@SuppressWarnings("deprecation")
	public void itemStack(ItemStack stack, String translation) {
		var itemName = stack.getItemName();
		if(!(itemName.getContents() instanceof TranslatableContents translatableContents)) {
			throw new IllegalArgumentException("Item name not translatable for [%s]: %s".formatted(stack.getItem().builtInRegistryHolder().key().identifier(), itemName));
		}

		var translationKey = translatableContents.getKey();
		if(translationKey.equals(stack.getItem().getDescriptionId())) {
			throw new IllegalArgumentException("Item translation %s is the default key, use item()/block() instead!".formatted(translationKey));
		}

		add(translatableContents.getKey(), translation);
	}

	public void mobEffect(Supplier<? extends MobEffect> effect, String translation) {
		add(effect.get().getDescriptionId(), translation);
	}

	public void damageType(ResourceKey<DamageType> typeKey, String defaultTranslation, @Nullable String killedByTranslation, @Nullable String killedWithItemTranslation) {
		var damageTypes = registries.lookupOrThrow(Registries.DAMAGE_TYPE);
		var type = damageTypes.getOrThrow(typeKey).value();

		if(type.deathMessageType() != DeathMessageType.DEFAULT) {
			throw new IllegalArgumentException("Death message type not currently supported: " + type.deathMessageType());
		}

		var translationKey = "death.attack." + type.msgId();
		add(translationKey, defaultTranslation);
		add(translationKey + ".player", killedByTranslation != null ? killedByTranslation : defaultTranslation);
		add(translationKey + ".stack", killedWithItemTranslation != null ? killedWithItemTranslation : defaultTranslation);
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

	public <T> HolderLookup.RegistryLookup<T> lookupOrThrow(ResourceKey<? extends Registry<? extends T>> registry) {
		return registries.lookupOrThrow(registry);
	}
}
