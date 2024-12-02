package dev.upcraft.sparkweave.api.event;

import dev.upcraft.sparkweave.event.EventFactoryImpl;

import java.util.function.Function;

public interface Event<T> {

	static <T> Event<T> create(Class<T> type, Function<T[], T> invokerFactory) {
		return EventFactoryImpl.create(type, invokerFactory);
	}

	static <T> Event<T> create(Class<T> type, T emptyInvoker, Function<T[], T> invokerFactory) {
		return EventFactoryImpl.create(type, emptyInvoker, invokerFactory);
	}

	void register(T listener);

	void unregister(T listener);

	T invoker();

	int listenerCount();
}
