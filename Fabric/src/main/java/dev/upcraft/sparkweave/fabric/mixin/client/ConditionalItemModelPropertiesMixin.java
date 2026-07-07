package dev.upcraft.sparkweave.fabric.mixin.client;

import dev.upcraft.sparkweave.api.client.event.RegisterItemModelPropertiesEvent;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConditionalItemModelProperties.class)
public class ConditionalItemModelPropertiesMixin {

	@Inject(method = "bootstrap", at = @At("RETURN"))
	private static void registerProperties(CallbackInfo ci) {
		RegisterItemModelPropertiesEvent.CONDITIONAL.invoker().registerProperties(ConditionalItemModelProperties.ID_MAPPER::put);
	}
}
