package dev.upcraft.sparkweave.api.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class SparkweaveLanguageProvider extends FabricLanguageProvider {

	private final CompletableFuture<HolderLookup.Provider> registriesFuture;

	public SparkweaveLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture, String languageCode) {
		super(dataOutput, languageCode);
		this.registriesFuture = registriesFuture;
	}

	protected void advancement(FabricLanguageProvider.TranslationBuilder translationBuilder, ResourceLocation advancementId, String title, String description) {
		translationBuilder.add(Util.makeDescriptionId("advancements", advancementId.withSuffix(".title")), title);
		translationBuilder.add(Util.makeDescriptionId("advancements", advancementId.withSuffix(".description")), description);
	}

	protected void biome(FabricLanguageProvider.TranslationBuilder translationBuilder, ResourceKey<Biome> biome, String translation) {
		translationBuilder.add(Util.makeDescriptionId("biome", biome.location()), translation);
	}

	protected void damageType(FabricLanguageProvider.TranslationBuilder translationBuilder, ResourceKey<DamageType> typeKey, String defaultTranslation, @Nullable String killedByTranslation, @Nullable String killedWithItemTranslation) {
		registriesFuture.thenAccept(registries -> {
			var damageTypes = registries.lookupOrThrow(Registries.DAMAGE_TYPE);
			var type = damageTypes.getOrThrow(typeKey).value();

			if(type.deathMessageType() != DeathMessageType.DEFAULT) {
				throw new IllegalArgumentException("Death message type not currently supported: " + type.deathMessageType());
			}

			var translationKey = "death.attack." + type.msgId();
			translationBuilder.add(translationKey, defaultTranslation);
			translationBuilder.add(translationKey + ".player", Objects.requireNonNullElse(killedByTranslation, defaultTranslation));
			translationBuilder.add(translationKey + ".item", Objects.requireNonNullElse(killedWithItemTranslation, defaultTranslation));
		});
	}

	protected void enchantment(FabricLanguageProvider.TranslationBuilder translationBuilder, Supplier<? extends Enchantment> enchantment, String translation, String description) {
		translationBuilder.add(enchantment.get().getDescriptionId(), translation);
		translationBuilder.add(enchantment.get().getDescriptionId() + ".desc", description);
	}

	protected void itemStack(FabricLanguageProvider.TranslationBuilder translationBuilder, ItemStack stack, String translationValue) {
		translationBuilder.add(stack.getDescriptionId(), translationValue);
	}

	protected void sound(FabricLanguageProvider.TranslationBuilder translationBuilder, Supplier<? extends SoundEvent> sound, String translation) {
		translationBuilder.add(Util.makeDescriptionId("subtitles", sound.get().getLocation()), translation);
	}

	protected void tag(FabricLanguageProvider.TranslationBuilder translationBuilder, TagKey<?> tag, String translation) {
		var registryName = tag.registry().location().toShortLanguageKey().replace('/', '.');
		var tagName = Util.makeDescriptionId("tag." + registryName, tag.location());
		translationBuilder.add(tagName, translation);
	}
}
