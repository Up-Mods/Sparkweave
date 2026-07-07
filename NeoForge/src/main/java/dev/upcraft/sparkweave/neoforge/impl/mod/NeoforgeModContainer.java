package dev.upcraft.sparkweave.neoforge.impl.mod;

import dev.upcraft.sparkweave.api.platform.ModContainer;

public record NeoforgeModContainer(NeoForgeModMetadata metadata) implements ModContainer {

    public static NeoforgeModContainer of(net.neoforged.fml.ModContainer delegate) {
        return new NeoforgeModContainer(new NeoForgeModMetadata(delegate.getModInfo()));
    }

//	@Override
//	public List<Path> rootPaths() {
//		var contents = metadata().modInfo().getOwningFile().getFile().getContents();
//		return contents.;
//	}
//
//	@Override
//	public Optional<Path> findPath(String path) {
//		return metadata().modInfo().getOwningFile().getFile().getContents().get(path);
//	}
}
