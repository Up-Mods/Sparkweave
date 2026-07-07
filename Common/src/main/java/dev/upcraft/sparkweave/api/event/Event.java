package dev.upcraft.sparkweave.api.event;

import dev.upcraft.sparkweave.event.EventFactoryImpl;

import java.util.function.Function;

public interface Event<T> {

	static <T> Event<T> create(Class<? super T> storageType, Function<T[], T> invokerFactory) {
		return EventFactoryImpl.create(storageType, invokerFactory);
	}

	static <T> Event<T> create(Class<? super T> storageType, T emptyInvoker, Function<T[], T> invokerFactory) {
		return EventFactoryImpl.create(storageType, emptyInvoker, invokerFactory);
	}

	void register(T listener);

	void unregister(T listener);

	T invoker();

	int listenerCount();
}
