package dev.upcraft.sparkweave.neoforge.mixin.internal;

import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

	@SuppressWarnings("unchecked")
	public <S extends T> RegistrySupplier<S> handler$register(String name, Supplier<S> factory) {
		return (RegistrySupplier<S>) this.register(name, factory);
	}

	@SuppressWarnings("unchecked")
	public Map<ResourceLocation, RegistrySupplier<? extends T>> handler$values() {
		return (Map<ResourceLocation, RegistrySupplier<? extends T>>) (Object) this.getEntries().stream().collect(Collectors.toMap(DeferredHolder::getId, UnaryOperator.identity()));
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
}
