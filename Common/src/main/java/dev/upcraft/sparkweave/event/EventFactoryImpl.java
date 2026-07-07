package dev.upcraft.sparkweave.event;

import com.google.common.base.Preconditions;
import dev.upcraft.sparkweave.api.event.Event;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A Simple event system.
 * @implNote The event system is designed to be as simple as possible, and is not thread safe.
 */
public class EventFactoryImpl<S, T extends S> implements Event<T> {

	private final Class<S> storageType;
	private final Function<T[], T> invokerFactory;
	private S[] listeners;
	private T invoker;

	private EventFactoryImpl(Class<S> storageType, Function<T[], T> invokerFactory) {
		this.storageType = storageType;
		this.invokerFactory = invokerFactory;
		this.listeners = makeArray(0);
		setupInvoker();
	}

	public static <S, T extends S> Event<T> create(Class<S> storageType, Function<T[], T> invokerFactory) {
		return new EventFactoryImpl<>(storageType, invokerFactory);
	}

	public static <S, T extends S> Event<T> create(Class<S> storageType, T emptyInvoker, Function<T[], T> invokerFactory) {
		return new EventFactoryImpl<>(storageType, listeners -> switch (listeners.length) {
			case 0 -> emptyInvoker;
			case 1 -> listeners[0];
			default -> invokerFactory.apply(listeners);
		});
	}

	@Override
	public void register(T listener) {
		Preconditions.checkArgument(storageType.isInstance(listener), "Listener is not of the correct type, must extend " + storageType.getName());
		Preconditions.checkArgument(Stream.of(listeners).noneMatch(it -> it == listener), "Listener is already registered");

		listeners = Arrays.copyOf(listeners, listeners.length + 1);
		listeners[listeners.length - 1] = listener;

		setupInvoker();
	}

	@Override
	public void unregister(T listener) {
		if (!storageType.isInstance(listener)) {
			return;
		}

		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] == listener) {
				listeners[i] = null;
				break;
			}
		}

		listeners = Arrays.stream(listeners).filter(Objects::nonNull).toArray(this::makeArray);

		setupInvoker();
	}

	@SuppressWarnings("unchecked")
	private void setupInvoker() {
		invoker = invokerFactory.apply((T[]) listeners);
	}

	@SuppressWarnings("unchecked")
	private S[] makeArray(int size) {
		return (S[]) Array.newInstance(storageType, size);
	}

	@Override
	public T invoker() {
		return invoker;
	}

	@Override
	public int listenerCount() {
		return listeners.length;
	}
}
