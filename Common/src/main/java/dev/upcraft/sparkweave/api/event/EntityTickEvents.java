package dev.upcraft.sparkweave.api.event;

import dev.upcraft.sparkweave.event.EntityTickEventsImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityTickEvents {

	public static <T extends Entity> Event<StartTick<T>> startTick(Class<T> entityClazz) {
		return EntityTickEventsImpl.getOrCreateStartTick(entityClazz);
	}

	public static <T extends Entity> Event<EndTick<T>> endTick(Class<T> entityClazz) {
		return EntityTickEventsImpl.getOrCreateEndTick(entityClazz);
	}

	@FunctionalInterface
	public interface StartTick<T extends Entity> {
		/**
		 * Fires at the very start of an entity being ticked
		 * @param entity the entity being ticked
		 * @param level the level the entity is in
		 * @return {@code true} to cancel the tick event for this entity. It's advised to not cancel a tick indiscriminately as it can break the game and other mods.
		 */
		boolean startTick(T entity, Level level);
	}

	@FunctionalInterface
	public interface EndTick<T extends Entity> {
		/**
		 * Fires at the very end of an entity being ticked
		 * @param entity the entity being ticked
		 * @param level the level the entity is in
		 */
		void endTick(T entity, Level level);
	}
}
