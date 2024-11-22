package dev.upcraft.sparkweave.api.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityTickEvents {
	public static final Event<StartTick> START_TICK = Event.create(StartTick.class, listeners -> (entity, level) -> {
		for(StartTick listener : listeners) {
			if(listener.startOfTick(entity, level))
				return true;
		}

		return false;
	});

	public static final Event<EndTick> END_TICK = Event.create(EndTick.class, listeners -> (entity, level) -> {
		for(EndTick listener : listeners)
			listener.endOfTick(entity, level);
	});

	@FunctionalInterface
	public interface StartTick {
		/**
		 * Fires at the very start of an entity being ticked
		 * @param entity the entity being ticked
		 * @param level the level the entity is in
		 * @return returns whether to cancel the tick event for the entity. It's advised to not cancel a tick indiscriminately as it can break the game and other mods.
		 */
		boolean startOfTick(Entity entity, Level level);
	}

	@FunctionalInterface
	public interface EndTick {
		/**
		 * Fires at the very end of an entity being ticked
		 * @param entity the entity being ticked
		 * @param level the level the entity is in
		 */
		void endOfTick(Entity entity, Level level);
	}
}
