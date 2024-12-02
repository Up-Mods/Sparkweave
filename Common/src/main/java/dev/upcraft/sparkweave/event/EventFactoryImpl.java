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
public class EventFactoryImpl<T> implements Event<T> {

	private final Class<T> type;
	private final Function<T[], T> invokerFactory;
	private T[] listeners;
	private T invoker;

	private EventFactoryImpl(Class<T> type, Function<T[], T> invokerFactory) {
		this.type = type;
		this.invokerFactory = invokerFactory;
		this.listeners = makeArray(0);
		setupInvoker();
	}

	public static <T> Event<T> create(Class<T> type, Function<T[], T> invokerFactory) {
		return new EventFactoryImpl<>(type, invokerFactory);
	}

	public static <T> Event<T> create(Class<T> type, T emptyInvoker, Function<T[], T> invokerFactory) {
		return new EventFactoryImpl<>(type, listeners -> switch (listeners.length) {
			case 0 -> emptyInvoker;
			case 1 -> listeners[0];
			default -> invokerFactory.apply(listeners);
		});
	}

	@Override
	public void register(T listener) {
		Preconditions.checkArgument(type.isInstance(listener), "Listener is not of the correct type, must extend " + type.getName());
		Preconditions.checkArgument(Stream.of(listeners).noneMatch(it -> it == listener), "Listener is already registered");

		listeners = Arrays.copyOf(listeners, listeners.length + 1);
		listeners[listeners.length - 1] = listener;

		setupInvoker();
	}

	@Override
	public void unregister(T listener) {
		if (!type.isInstance(listener)) {
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

	private void setupInvoker() {
		invoker = invokerFactory.apply(listeners);
	}

	@SuppressWarnings("unchecked")
	private T[] makeArray(int size) {
		return (T[]) Array.newInstance(type, size);
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
