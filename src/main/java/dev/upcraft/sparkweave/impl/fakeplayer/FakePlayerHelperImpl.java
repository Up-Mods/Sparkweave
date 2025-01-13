package dev.upcraft.sparkweave.impl.fakeplayer;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.function.Predicate;

public class FakePlayerHelperImpl {

	private static final List<Predicate<Player>> FAKE_PLAYER_CHECKS = new ObjectArrayList<>();

	public static void registerCheck(Predicate<Player> check) {
		FAKE_PLAYER_CHECKS.add(check);
	}

	public static boolean isFakePlayer(Player player) {
		for (Predicate<Player> fakePlayerCheck : FAKE_PLAYER_CHECKS) {
			if (fakePlayerCheck.test(player)) {
				return true;
			}
		}

		return false;
	}

	static {
		registerCheck(player -> player instanceof FakePlayer);
	}
}
