package dev.upcraft.sparkweave.api.platform;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import dev.upcraft.sparkweave.api.annotation.CallerSensitive;
import dev.upcraft.sparkweave.api.reflect.ContextHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Env {

	@Nullable
	@CallerSensitive
	public static String get(String name) {
		var caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
		var ctx = ContextHelper.getContext(caller);
		return get(name, ctx.metadata().id());
	}

	@Nullable
	public static String get(String name, String prefix) {
		if(Strings.isNullOrEmpty(prefix)) {
			return System.getProperty(name);
		}

		return System.getProperty(prefix + '.' + name);
	}

	public static String get(String name, String prefix, String defaultValue) {
		Preconditions.checkNotNull(defaultValue, "no default value provided");

		return Objects.requireNonNullElse(get(name, prefix), defaultValue);
	}

	@CallerSensitive
	public static boolean getBool(String name) {
		var caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
		var ctx = ContextHelper.getContext(caller);
		return getBool(name, ctx.metadata().id());
	}

	public static boolean getBool(String name, String prefix) {
		if (Strings.isNullOrEmpty(prefix)) {
			return Boolean.getBoolean(name);
		}

		return Boolean.getBoolean(prefix + '.' + name);
	}

	@CallerSensitive
	public static int getInt(String name) {
		var caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
		var ctx = ContextHelper.getContext(caller);
		return getInt(name, ctx.metadata().id());
	}

	public static int getInt(String name, String prefix) {
		if (Strings.isNullOrEmpty(prefix)) {
			return Integer.getInteger(name, 0);
		}

		return Integer.getInteger(prefix + '.' + name, 0);
	}


}
