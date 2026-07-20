package dev.upcraft.sparkweave.neoforge.mixin.ext;

import dev.upcraft.sparkweave.api.ext.TagKeyExt;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(TagKey.class)
public abstract class TagKeyMixin<T> implements TagKeyExt {

	@Nullable
	@Unique
	private String sparkweave$descriptionId;

	@Shadow
	public abstract Identifier location();

	@Shadow
	public abstract ResourceKey<? extends Registry<T>> registry();

	@Override
	public String getDescriptionId() {
		if (sparkweave$descriptionId == null) {
			sparkweave$descriptionId = Util.makeDescriptionId("tag.%s".formatted(this.registry().identifier().toShortLanguageKey()), this.location());
		}
		return sparkweave$descriptionId;
	}

	@Override
	public Component getName() {
		return Component.translatableWithFallback(getDescriptionId(), "#%s".formatted(location()));
	}
}
