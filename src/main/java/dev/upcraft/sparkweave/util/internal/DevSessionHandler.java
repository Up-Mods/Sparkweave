package dev.upcraft.sparkweave.util.internal;

import com.mojang.util.UndashedUuid;
import dev.upcraft.sparkweave.api.SparkweaveApi;
import dev.upcraft.sparkweave.api.util.Env;
import net.minecraft.client.User;
import net.minecraft.core.UUIDUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.UUID;

public class DevSessionHandler {

	public static Optional<User> tryLoadSession(User previous) {
		if(SparkweaveApi.DEVELOPMENT_ENVIRONMENT) {
			String name = Env.get("debug.session.username");
			String uuidString = Env.get("debug.session.uuid");
			if(name == null && StringUtils.isBlank(uuidString)) return Optional.empty();
			UUID uuid = Optional.ofNullable(uuidString).map(UndashedUuid::fromStringLenient).orElseGet(() -> UUIDUtil.createOfflinePlayerUUID(name));
			return Optional.of(new User(name, uuid, previous.getAccessToken(), previous.getXuid(), previous.getClientId(), previous.getType()));
		}
		return Optional.empty();
	}
}
