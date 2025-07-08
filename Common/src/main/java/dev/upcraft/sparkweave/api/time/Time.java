package dev.upcraft.sparkweave.api.time;

import java.time.Duration;

// TODO conversions for tickrate-dependent things
public class Time {

	@Deprecated
	public static long toTicks(long durationMs) {
		return durationMs / 50L;
	}

	@Deprecated
	public static long toMillis(long durationTicks) {
		return durationTicks * 50L;
	}

	@Deprecated
	public static long toTicks(Duration duration) {
		return toTicks(duration.toMillis());
	}
}
