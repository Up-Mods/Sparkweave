package dev.upcraft.sparkweave.api.datagen.provider;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import dev.upcraft.sparkweave.api.datagen.ContextAwarePackOutput;
import dev.upcraft.sparkweave.api.datagen.TranslationBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.locale.Language;
import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public abstract class SparkweaveLanguageProvider implements DataProvider {

	private final Map<String, String> extraTranslations = Collections.synchronizedMap(new TreeMap<>());
	private static final Pattern LANGUAGE_FILE_PATTERN = Pattern.compile("^[a-z][a-z0-9]+_[a-z0-9]{2,}$");
	private static final Codec<Map<String, String>> LANGUAGE_FILE_CODEC = Codec.unboundedMap(Codec.STRING, Codec.STRING);
	private final CompletableFuture<HolderLookup.Provider> registriesFuture;
	private final String languageCode;
	private final ContextAwarePackOutput output;

	public SparkweaveLanguageProvider(ContextAwarePackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, String languageCode) {
		Preconditions.checkArgument(LANGUAGE_FILE_PATTERN.asMatchPredicate().test(languageCode), "Invalid language code: %s", languageCode);
		this.output = output;
		this.registriesFuture = registriesFuture;
		this.languageCode = languageCode;
	}

	public abstract void generateTranslations(HolderLookup.Provider registries, TranslationBuilder builder);

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		return registriesFuture.thenCompose(registries -> {
			var builder = new TranslationBuilder(registries);

			// add extra translations before manual ones!
			extraTranslations.forEach(builder::add);

			generateTranslations(registries, builder);

			var pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "lang");
			var outputPath = pathProvider.json(Identifier.fromNamespaceAndPath(output.getModContainer().metadata().id(), languageCode));
			return DataProvider.saveStable(cachedOutput, registries, LANGUAGE_FILE_CODEC, builder.build(), outputPath);
		});
	}

	@Override
	public String getName() {
		return "Language Provider/%s/%s".formatted(output.getModContainer().metadata().displayName(), languageCode);
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public boolean isDefaultLanguage() {
		return languageCode.equals(Language.DEFAULT);
	}

	public void addExtra(String key, String translation) {
		if(!isDefaultLanguage()) {
			throw new IllegalStateException("Attempted to add auto translation to %s language provider, can only add translations to default (%s) language!".formatted(languageCode, Language.DEFAULT));
		}

		var prev = extraTranslations.putIfAbsent(key, translation);
		if(prev != null) {
			throw new IllegalArgumentException("Translation key '%s' was already translated!".formatted(key));
		}
	}
}
