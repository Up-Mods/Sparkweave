package dev.upcraft.sparkweave.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public final class Mod {

	private Mod() {
		throw new UnsupportedOperationException();
	}

	@Target(value = {ElementType.TYPE, ElementType.PACKAGE})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Context {
		/**
		 * @return the mod ID of the current context
		 */
		String value();
	}
}
