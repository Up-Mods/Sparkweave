package dev.upcraft.sparkweave.neoforge.mixin;

import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DeferredHolder.class)
public abstract class DeferredHolderMixin<R, T extends R> implements RegistrySupplier<T> {

	@Invoker("is")
	@Override
	public abstract boolean matches(TagKey<? super T> tag);

	@Invoker("getKey")
	@Override
	public abstract ResourceKey<? super T> getRegistryKey();

	@SuppressWarnings({"unchecked", "AddedMixinMembersNamePattern"})
	@Override
	public <R2> Holder<R2> holder() {
		return (Holder<R2>) this;
	}
}
