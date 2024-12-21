package dev.upcraft.sparkweave.mixin.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MixinRemoveAuthError {

	@Redirect(method = "createUserApiService", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V"), remap = false)
	private void error(Logger logger, String message, Throwable t) {
		logger.debug(message);
	}
}
