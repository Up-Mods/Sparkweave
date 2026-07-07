package dev.upcraft.sparkweave.platform;

import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.ModMetadata;

public class SimpleModContainer implements ModContainer {

	private final ModMetadata metadata;
//	private final List<Path> rootPaths;
//	private final Function<String, Optional<Path>> pathFinder;

	public SimpleModContainer(ModMetadata metadata/*, List<Path> rootPaths, Function<String, Optional<Path>> pathFinder*/) {
		this.metadata = metadata;
//		this.rootPaths = rootPaths;
//		this.pathFinder = pathFinder;
	}

	@Override
	public ModMetadata metadata() {
		return metadata;
	}

//	@Override
//	public List<Path> rootPaths() {
//		return rootPaths;
//	}
//
//	@Override
//	public Optional<Path> findPath(String path) {
//		return pathFinder.apply(path);
//	}
}
