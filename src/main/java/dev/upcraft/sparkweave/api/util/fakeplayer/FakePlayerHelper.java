package dev.upcraft.sparkweave.api.util.fakeplayer;

import dev.upcraft.sparkweave.impl.fakeplayer.FakePlayerHelperImpl;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class FakePlayerHelper {

	public static boolean isFakePlayer(Player player) {
		return FakePlayerHelperImpl.isFakePlayer(player);
	}

	// TODO replace with event in 1.21
	public static void registerCheck(Predicate<Player> check) {
		FakePlayerHelperImpl.registerCheck(check);
	}
}
