package dev.upcraft.sparkweave.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class SparkweaveMixinConfigPlugin implements IMixinConfigPlugin {

	private static final String PACKAGE_NAME = "dev.upcraft.sparkweave.mixin";
	private static final boolean DEV_ENV = FabricLoader.getInstance().isDevelopmentEnvironment();

	@Override
	public void onLoad(String mixinPackage) {

	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if(mixinClassName.startsWith(PACKAGE_NAME)) {
			if(!DEV_ENV) {
				boolean isDebugMixin = mixinClassName.substring(PACKAGE_NAME.length() + 1).split("\\.", 2)[0].equalsIgnoreCase("debug");
				return !isDebugMixin;
			}

			return true;
		}
		return false;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}
}
