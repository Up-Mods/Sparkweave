package dev.upcraft.sparkweave.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.upcraft.sparkweave.fabric.impl.registry.FabricRegistryHack;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> implements FabricRegistryHack<T>, HolderOwner<T> {

	@Unique
	private final Map<ResourceKey<T>, Holder.Reference<T>> unregisteredHolders = new HashMap<>();

	@Shadow
	protected abstract void validateWrite(ResourceKey<T> key);

	@Shadow
	public abstract ResourceKey<? extends Registry<T>> key();

	@Shadow
	@Nullable
	private Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders;

	@Shadow
	public abstract Holder.Reference<T> createIntrusiveHolder(T value);

	@Override
	public Holder.Reference<T> sparkweave$createHolder(ResourceKey<T> key, Supplier<T> factory) {
		if(unregisteredIntrusiveHolders != null) {
			return createIntrusiveHolder(factory.get());
		}
		validateWrite(key);
		var holder = Holder.Reference.createStandAlone(this, key);
		unregisteredHolders.put(key, holder);
		return holder;
	}

	@WrapOperation(method = "lambda$register$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Holder$Reference;createStandAlone(Lnet/minecraft/core/HolderOwner;Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/core/Holder$Reference;"))
	private Holder.Reference<T> wrapCreateReference(HolderOwner<T> owner, ResourceKey<T> key, Operation<Holder.Reference<T>> original) {
		var value = unregisteredHolders.remove(key);
		if(value != null) {
			return value;
		}

		return original.call(owner, key);
	}

	@Inject(method = "freeze", at = @At(value = "FIELD", target = "Lnet/minecraft/core/MappedRegistry;frozen:Z", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
	private void validateOnFreeze(CallbackInfoReturnable<Registry<T>> cir) {
		if(!unregisteredHolders.isEmpty()) {
			throw new IllegalStateException("Unbound values in registry " + this.key() + ": " + unregisteredHolders.keySet().stream().map(Object::toString).collect(Collectors.joining(",")));
		}
	}
}
