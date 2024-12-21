package dev.upcraft.sparkweave.mixin.debug;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import dev.upcraft.sparkweave.api.SparkweaveApi;
import dev.upcraft.sparkweave.util.SparkweaveLogging;
import dev.upcraft.sparkweave.util.internal.DevSessionHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MixinDeveloperSession {

	@Inject(method = "createUserApiService", at = @At("HEAD"))
	private void construct(YggdrasilAuthenticationService yggdrasilAuthenticationService, GameConfig config, CallbackInfoReturnable<UserApiService> cir) {
		if (SparkweaveApi.DEVELOPMENT_ENVIRONMENT) {
			DevSessionHandler.tryLoadSession(config.user.user).ifPresent(s -> {
				SparkweaveLogging.getLogger().warn("Updating session state! Profile changed to: {}({})", s.getName(), s.getUuid());
				((UserDataAccessor) config.user).sw_updateSession(s);
			});
		}
	}
}
