package dev.upcraft.sparkweave.datagen;

import com.google.gson.JsonElement;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class SparkweaveSimpleModelCollector implements BiConsumer<Identifier, ModelInstance> {
	private final Map<Identifier, ModelInstance> models = new HashMap<>();

	public SparkweaveSimpleModelCollector() {
	}

	public void accept(Identifier id, ModelInstance contents) {
		Supplier<JsonElement> prev = this.models.put(id, contents);
		if (prev != null) {
			throw new IllegalStateException("Duplicate model definition for %s".formatted(id));
		}
	}

	public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
		Objects.requireNonNull(pathProvider);
		return DataProvider.saveAll(cache, Supplier::get, pathProvider::json, this.models);
	}
}
