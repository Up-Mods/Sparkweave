package dev.upcraft.sparkweave.mixin.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.User;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(GameConfig.UserData.class)
public interface UserDataAccessor {

	@Mutable
	@Accessor("user")
	void sw_updateSession(User value);
}
