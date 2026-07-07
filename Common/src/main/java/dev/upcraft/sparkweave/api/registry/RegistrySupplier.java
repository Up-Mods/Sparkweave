package dev.upcraft.sparkweave.api.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.function.Supplier;

public interface RegistrySupplier<T> extends Supplier<T> {

	@Override
	T get();

	default boolean is(T other) {
		return this.get() == other;
	}

	default boolean is(Supplier<T> other) {
		if(other instanceof RegistrySupplier<T> registrySupplier) {
			return this.getRegistryKey() == registrySupplier.getRegistryKey();
		}

		// only resolve if needed
		return is(other.get());
	}

	boolean matches(TagKey<? super T> tag);

	Identifier getId();

	ResourceKey<? super T> getRegistryKey();

	Registry<? super T> getRegistry();

	<R> Holder<R> holder();
}
