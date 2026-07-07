package dev.upcraft.sparkweave.neoforge.mixin;

import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Implements(@Interface(iface = RegistryHandler.class, prefix = "handler$"))
@Mixin(DeferredRegister.class)
public abstract class DeferredRegisterMixin<T> {

	@Shadow
	public abstract Collection<DeferredHolder<T, ? extends T>> getEntries();

	@Shadow
	public abstract <I extends T> DeferredHolder<T, I> register(String name, Supplier<? extends I> sup);

	@Shadow
	public abstract Registry<T> makeRegistry(Consumer<RegistryBuilder<T>> consumer);

	@Shadow
	public abstract String getNamespace();

	@SuppressWarnings("unchecked")
	public <S extends T> RegistrySupplier<S> handler$register(String name, Supplier<S> factory) {
		return (RegistrySupplier<S>) this.register(name, factory);
	}

	@SuppressWarnings("unchecked")
	public <S extends T> RegistrySupplier<S> handler$register(ResourceKey<T> id, Supplier<S> factory) {
		if(!this.getNamespace().equals(id.identifier().getNamespace())) {
			throw new IllegalArgumentException("Cannot register %s because namespace does not match the expected value %s".formatted(id, this.getNamespace()));
		}

		return (RegistrySupplier<S>) register(id.identifier().getPath(), factory);
	}

	@SuppressWarnings("unchecked")
	public Map<Identifier, RegistrySupplier<? extends T>> handler$values() {
		return (Map<Identifier, RegistrySupplier<? extends T>>) (Object) this.getEntries().stream().collect(Collectors.toMap(DeferredHolder::getId, UnaryOperator.identity()));
	}

	@SuppressWarnings("unchecked")
	public List<RegistrySupplier<? extends T>> handler$getEntriesOrdered() {
		return (List<RegistrySupplier<? extends T>>) (Object) this.getEntries().stream().sorted(Comparator.comparing(DeferredHolder::getKey, Comparator.naturalOrder())).toList();
	}

	@SuppressWarnings("unchecked")
	public Stream<RegistrySupplier<? extends T>> handler$stream() {
		return (Stream<RegistrySupplier<? extends T>>) (Object) this.getEntries().stream();
	}

	@Invoker("getRegistryKey")
	public abstract ResourceKey<Registry<T>> handler$registry();

	public Registry<T> handler$createNewRegistry(boolean synced, @Nullable Identifier defaultEntry) {
		return this.makeRegistry(builder -> {
			if(defaultEntry != null) {
				builder.defaultKey(defaultEntry);
			}

			builder.sync(synced);
		});
	}
}
