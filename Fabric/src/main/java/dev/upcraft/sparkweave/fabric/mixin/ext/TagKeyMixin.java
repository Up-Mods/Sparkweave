package dev.upcraft.sparkweave.fabric.mixin.ext;

import dev.upcraft.sparkweave.api.ext.TagKeyExt;
import net.fabricmc.fabric.api.tag.FabricTagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(FabricTagKey.class)
public interface TagKeyMixin extends TagKeyExt {

	@Shadow
	String getTranslationKey();

	@Override
	default String getDescriptionId() {
		return this.getTranslationKey();
	}
}
