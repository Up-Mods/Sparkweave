package dev.upcraft.sparkweave.fabric.mixin.client;

import dev.upcraft.sparkweave.api.client.event.RegisterItemModelPropertiesEvent;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SelectItemModelProperties.class)
public class SelectItemModelPropertiesMixin {

	@Inject(method = "bootstrap", at = @At("RETURN"))
	private static void registerProperties(CallbackInfo ci) {
		RegisterItemModelPropertiesEvent.SELECT.invoker().registerProperties(SelectItemModelProperties.ID_MAPPER::put);
	}
}
