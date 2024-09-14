package dev.upcraft.sparkweave.fabric.impl.registry;

import com.google.common.base.Suppliers;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistryHelper;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FabricRegistryHandler<T> implements RegistryHandler<T> {

	private final ResourceKey<Registry<T>> registryKey;
	private final String namespace;
	private final Supplier<Registry<T>> registry;

	private final Map<ResourceLocation, RegistrySupplier<? extends T>> values = new Object2ObjectOpenHashMap<>();
	private final List<RegistrySupplier<? extends T>> orderedEntries = new LinkedList<>();

	private FabricRegistryHandler(ResourceKey<Registry<T>> registryKey, String namespace) {
		this.registryKey = registryKey;
		this.namespace = namespace;
		this.registry = Suppliers.memoize(() -> RegistryHelper.getBuiltinRegistry(registryKey));
	}

	public static <T> FabricRegistryHandler<T> create(ResourceKey<Registry<T>> registryKey, String modid) {
		return new FabricRegistryHandler<>(registryKey, modid);
	}

	@Override
	public <S extends T> FabricRegistrySupplier<T, S> register(String name, Supplier<S> factory) {
		var id = ResourceLocation.fromNamespaceAndPath(namespace, name);
		FabricRegistrySupplier<T, S> supplier = new FabricRegistrySupplier<>(ResourceKey.create(registryKey, id), factory);

		// immediately register entry
		supplier.register(registry.get());

		values.put(id, supplier);
		orderedEntries.add(supplier);
		return supplier;
	}

	@Override
	public Map<ResourceLocation, RegistrySupplier<? extends T>> values() {
		return Collections.unmodifiableMap(values);
	}

	@Override
	public List<RegistrySupplier<? extends T>> getEntriesOrdered() {
		return Collections.unmodifiableList(orderedEntries);
	}

	@Override
	public Stream<RegistrySupplier<? extends T>> stream() {
		return orderedEntries.stream();
	}

	@Override
	public ResourceKey<Registry<T>> registry() {
		return registryKey;
	}

	@Override
	public Registry<T> createNewRegistry(boolean sync, @Nullable ResourceLocation defaultEntry) {
		var builder = defaultEntry != null ? FabricRegistryBuilder.createDefaulted(this.registryKey, defaultEntry) : FabricRegistryBuilder.createSimple(this.registryKey);
		if(sync) {
			builder.attribute(RegistryAttribute.SYNCED);
		}
		return builder.buildAndRegister();
	}
}
