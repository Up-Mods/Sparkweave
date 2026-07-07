package dev.upcraft.sparkweave.api.client.event;

import com.mojang.serialization.MapCodec;
import dev.upcraft.sparkweave.api.event.Event;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;

public interface RegisterItemModelPropertiesEvent<T> {

	void register(Identifier id, MapCodec<? extends T> codec);

	/**
	 * func -> float
	 */
	Event<Callback<RangeSelectItemModelProperty>> RANGED = Event.create(RegisterItemModelPropertiesEvent.Callback.class, listeners -> event -> {
		for (var callback : listeners) {
			callback.registerProperties(event);
		}
	});

	/**
	 * func -> boolean
	 */
	Event<Callback<ConditionalItemModelProperty>> CONDITIONAL = Event.create(RegisterItemModelPropertiesEvent.Callback.class, listeners -> event -> {
		for (var callback : listeners) {
			callback.registerProperties(event);
		}
	});

	/**
	 * func -> T
	 */
	Event<SelectCallback> SELECT = Event.create(RegisterItemModelPropertiesEvent.SelectCallback.class, listeners -> event -> {
		for (var callback : listeners) {
			callback.registerProperties(event);
		}
	});

	@FunctionalInterface
	interface Callback<T> {

		void registerProperties(RegisterItemModelPropertiesEvent<T> event);
	}

	interface Select {
		<P extends SelectItemModelProperty<T>, T> void register(Identifier id, SelectItemModelProperty.Type<P, T> property);
	}

	interface SelectCallback {
		void registerProperties(RegisterItemModelPropertiesEvent.Select event);
	}
}
