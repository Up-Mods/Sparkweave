package dev.upcraft.sparkweave.quilt.impl.registry;

import dev.upcraft.sparkweave.api.registry.RegistryHelper;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public class QuiltRegistrySupplier<R, T extends R> implements RegistrySupplier<T> {

	private final ResourceKey<? super T> id;
	private final Supplier<T> factory;
	@Nullable
	private Holder<R> holder;
	@Nullable
	private T value;

	private Registry<R> registry;

	public QuiltRegistrySupplier(ResourceKey<R> id, Supplier<T> factory) {
		this.id = id;
		this.factory = factory;
	}

	private T getOrCreateObject() {
		if (this.value == null) {
			this.value = Objects.requireNonNull(this.factory.get(), "factory returned null for " + this.id);
		}
		return this.value;
	}

	public void register(Registry<R> registry) {
		this.registry = registry;
		var object = getOrCreateObject();
		Registry.register(registry, this.getRegistryKey(), object);
		var entryHolder = registry.getHolderOrThrow(this.getRegistryKey());
		if (entryHolder instanceof Holder.Reference<R> reference && (reference.type != Holder.Reference.Type.INTRUSIVE || reference.value == null)) {
			reference.bindValue(this.value);
		}
		this.holder = entryHolder;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get() {
		if (value == null) {
			value = (T) Objects.requireNonNull(getRegistry().get(this.id.location()), "Registry supplier called too early: " + this.getId());
		}
		return value;
	}

	@Override
	public boolean is(T other) {
		return this.get() == other;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean matches(TagKey<? super T> tag) {
		return holder().is((TagKey<R>) tag);
	}

	@Override
	public ResourceLocation getId() {
		return this.id.location();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResourceKey<R> getRegistryKey() {
		return (ResourceKey<R>) this.id;
	}

	@Override
	public Registry<R> getRegistry() {
		if(registry == null) {
			registry = RegistryHelper.getBuiltinRegistry(ResourceKey.createRegistryKey(id.registry()));
		}
		return registry;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Holder<R> holder() {
		if (holder == null) {
			holder = registry.getHolder(getRegistryKey()).orElseGet(() -> {
				if (registry instanceof QuiltRegistryHack<?>) {
					return ((QuiltRegistryHack<R>) registry).sparkweave$createHolder(getRegistryKey(), this::getOrCreateObject);
				}

				throw new IllegalStateException("Unable to create holder for entry %s because registry is not a mapped registry!".formatted(getRegistryKey()));
			});
		}
		return holder;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof QuiltRegistrySupplier<?, ?> other) {
			return this.getRegistryKey() == other.getRegistryKey();
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id.registry(), this.id.location());
	}
}
