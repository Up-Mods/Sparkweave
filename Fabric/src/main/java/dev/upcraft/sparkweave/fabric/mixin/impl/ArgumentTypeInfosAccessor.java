package dev.upcraft.sparkweave.fabric.mixin.impl;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ArgumentTypeInfos.class)
public interface ArgumentTypeInfosAccessor {

	@Accessor("BY_CLASS")
	static Map<Class<?>, ArgumentTypeInfo<?, ?>> sparkweave$getByClass() {
		throw new UnsupportedOperationException("Mixin not transformed");
	}
}
