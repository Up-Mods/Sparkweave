package dev.upcraft.sparkweave.fabric.mixin.datagen;

import dev.upcraft.sparkweave.api.ext.datagen.RegistrySetBuilderExt;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(RegistrySetBuilder.class)
public abstract class RegistrySetBuilderMixin implements RegistrySetBuilderExt {

	@Shadow
	@Final
	private List<RegistrySetBuilder.RegistryStub<?>> entries;

	@SuppressWarnings("AddedMixinMembersNamePattern")
	@Override
	public List<? extends ResourceKey<? extends Registry<?>>> getEntryKeys() {
		return entries.stream().map(RegistrySetBuilder.RegistryStub::key).toList();
	}
}
