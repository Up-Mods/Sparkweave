package dev.upcraft.sparkweave.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.upcraft.sparkweave.api.event.EntityTickEvents;
import dev.upcraft.sparkweave.api.event.Event;
import net.minecraft.world.entity.Entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityTickEventsImpl {

	private static final Multimap<Class<? extends Entity>, EntityTickEvents.StartTick<Entity>> START_TICK_HANDLERS = HashMultimap.create();
	private static final Multimap<Class<? extends Entity>, EntityTickEvents.EndTick<Entity>> END_TICK_HANDLERS = HashMultimap.create();

	// Note: client and server threads tick independently of each other and therefore might be accessing these
	//  simultaneously, therefore the computed maps need to be threadsafe.
	//  see https://github.com/Up-Mods/Sparkweave/issues/4
	private static final Map<Class<? extends Entity>, Event<EntityTickEvents.StartTick<Entity>>> COMPUTED_START_HANDLERS = new ConcurrentHashMap<>();
	private static final Map<Class<? extends Entity>, Event<EntityTickEvents.EndTick<Entity>>> COMPUTED_END_HANDLERS = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public static <T extends Entity> Event<EntityTickEvents.StartTick<T>> getOrCreateStartTick(Class<T> entityClass) {
		return new Event<>() {
			@Override
			public void register(EntityTickEvents.StartTick<T> listener) {
				START_TICK_HANDLERS.put(entityClass, (EntityTickEvents.StartTick<Entity>) listener);
				COMPUTED_START_HANDLERS.remove(entityClass);
			}

			@Override
			public void unregister(EntityTickEvents.StartTick<T> listener) {
				START_TICK_HANDLERS.remove(entityClass, listener);
				COMPUTED_START_HANDLERS.remove(entityClass);
			}

			@Override
			public EntityTickEvents.StartTick<T> invoker() {
				return (EntityTickEvents.StartTick<T>) getStartHandler(entityClass).invoker();
			}

			@Override
			public int listenerCount() {
				var evt = COMPUTED_START_HANDLERS.get(entityClass);
				return evt != null ? evt.listenerCount() : 0;
			}
		};
	}

	@SuppressWarnings("unchecked")
	public static Event<EntityTickEvents.StartTick<Entity>> getStartHandler(Class<? extends Entity> entityClass) {
		return COMPUTED_START_HANDLERS.computeIfAbsent(entityClass, key -> {
			var eventHandler = (Event<EntityTickEvents.StartTick<Entity>>) (Object) Event.create(EntityTickEvents.StartTick.class, (entity, level) -> false, listeners -> (entity, level) -> {
				for (EntityTickEvents.StartTick<Entity> listener : listeners) {
					if (listener.startTick(entity, level)) {
						return true;
					}
				}

				return false;
			});

			Class<?> clazz = entityClass;
			while (Entity.class.isAssignableFrom(clazz)) {
				START_TICK_HANDLERS.get((Class<? extends Entity>) clazz).forEach(eventHandler::register);
				clazz = clazz.getSuperclass();
			}

			return eventHandler;
		});
	}

	@SuppressWarnings("unchecked")
	public static <T extends Entity> Event<EntityTickEvents.EndTick<T>> getOrCreateEndTick(Class<T> entityClass) {
		return new Event<>() {
			@Override
			public void register(EntityTickEvents.EndTick<T> listener) {
				END_TICK_HANDLERS.put(entityClass, (EntityTickEvents.EndTick<Entity>) listener);
				COMPUTED_END_HANDLERS.clear();
			}

			@Override
			public void unregister(EntityTickEvents.EndTick<T> listener) {
				END_TICK_HANDLERS.remove(entityClass, listener);
				COMPUTED_END_HANDLERS.clear();
			}

			@Override
			public EntityTickEvents.EndTick<T> invoker() {
				return (EntityTickEvents.EndTick<T>) getEndHandler(entityClass).invoker();
			}

			@Override
			public int listenerCount() {
				var evt = COMPUTED_END_HANDLERS.get(entityClass);
				return evt != null ? evt.listenerCount() : 0;
			}
		};
	}

	@SuppressWarnings("unchecked")
	public static Event<EntityTickEvents.EndTick<Entity>> getEndHandler(Class<? extends Entity> entityClass) {
		return COMPUTED_END_HANDLERS.computeIfAbsent(entityClass, key -> {
			var eventHandler = (Event<EntityTickEvents.EndTick<Entity>>) (Object) Event.create(EntityTickEvents.EndTick.class, (entity, level) -> {}, listeners -> (entity, level) -> {
				for (EntityTickEvents.EndTick<Entity> listener : listeners) {
					listener.endTick(entity, level);
				}
			});

			Class<?> clazz = entityClass;
			while (Entity.class.isAssignableFrom(clazz)) {
				END_TICK_HANDLERS.get((Class<? extends Entity>) clazz).forEach(eventHandler::register);
				clazz = clazz.getSuperclass();
			}

			return eventHandler;
		});
	}


}
