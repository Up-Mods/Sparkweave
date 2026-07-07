package dev.upcraft.sparkweave.api.registry;

import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface RegistryVisitor<T> extends BiConsumer<Identifier, T> {

	@Override
	void accept(Identifier id, T t);
}
