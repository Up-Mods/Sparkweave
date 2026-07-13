package dev.upcraft.sparkweave.neoforge.mixin.client.ext;

import dev.upcraft.sparkweave.api.client.ext.RenderStateExt;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.util.context.ContextKey;
import net.neoforged.neoforge.client.renderstate.BaseRenderState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(BaseRenderState.class)
public abstract class BaseRenderStateMixin implements RenderStateExt {

	@Unique
	private final Map<ContextKey<?>, Object> sparkweave$customRenderData = new Reference2ObjectOpenHashMap<>();

	@Override
	public <T> void sparkweave$setData(ContextKey<T> key, @Nullable T data) {
		if(data != null) {
			sparkweave$customRenderData.put(key, data);
		}
		else {
			sparkweave$customRenderData.remove(key);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public @Nullable <T> T sparkweave$getData(ContextKey<T> key) {
		return (T) sparkweave$customRenderData.get(key);
	}
}
