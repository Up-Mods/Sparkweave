package dev.upcraft.sparkweave.fabric.mixin.client;

import dev.upcraft.sparkweave.renderdoc.client.RenderDocHelper;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MainMixin {

	@Inject(method = "main", at = @At("HEAD"), remap = false)
	private static void loadRenderdoc(String[] args, CallbackInfo ci) {
		RenderDocHelper.init();
	}
}
