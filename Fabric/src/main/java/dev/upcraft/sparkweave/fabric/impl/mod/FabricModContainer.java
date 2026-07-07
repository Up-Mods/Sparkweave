package dev.upcraft.sparkweave.fabric.impl.mod;

import dev.upcraft.sparkweave.api.platform.ModContainer;

public record FabricModContainer(net.fabricmc.loader.api.ModContainer delegate, FabricModMetadata metadata) implements ModContainer {

    public static FabricModContainer of(net.fabricmc.loader.api.ModContainer delegate) {
        return new FabricModContainer(delegate, new FabricModMetadata(delegate));
    }

//	@Override
//	public List<Path> rootPaths() {
//		return delegate().getRootPaths();
//	}
//
//	@Override
//	public Optional<Path> findPath(String file) {
//		return delegate().findPath(file);
//	}
}
