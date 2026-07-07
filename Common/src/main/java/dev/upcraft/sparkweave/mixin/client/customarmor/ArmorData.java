package dev.upcraft.sparkweave.mixin.client.customarmor;

import dev.upcraft.sparkweave.api.client.render.CustomArmorRenderer;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public final class ArmorData {
	private ItemStack stack = ItemStack.EMPTY;
	private @Nullable CustomArmorRenderer<?, ?, ?, ?, ?> customRenderer;
	private Reference2ObjectArrayMap<ContextKey<?>, Object> customData = new Reference2ObjectArrayMap<>();

	public ArmorData() {
	}

	public @Nullable CustomArmorRenderer<?, ?, ?, ?, ?> getCustomRenderer() {
		return customRenderer;
	}

	public void setCustomRenderer(@Nullable CustomArmorRenderer<?, ?, ?, ?, ?> customRenderer) {
		this.customRenderer = customRenderer;
	}

	public <T> void setCustomData(ContextKey<T> key, @Nullable T data) {
		if(data != null) {
			customData.put(key, data);
		}
		else {
			customData.remove(key);
		}
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public <T> T getCustomData(ContextKey<T> key) {
		return (T) customData.get(key);
	}

	public ItemStack getStack() {
		return stack;
	}

	public void setStack(ItemStack stack) {
		this.stack = stack;
	}

	public void clear() {
		customRenderer = null;
		customData = new Reference2ObjectArrayMap<>();
	}
}
