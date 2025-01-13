package dev.upcraft.sparkweave.api.util.data;

import dev.upcraft.sparkweave.util.SparkweaveLogging;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Deprecated(forRemoval = true)
public class DataStore<T> {

	private final String name;
	@Nullable
	private T data;
	private final Supplier<T> prepareFunction;
	private final Duration cacheDuration;
	private final boolean allowStale;
	private Instant lastRefresh = Instant.MIN;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	private final Event<Consumer<T>> refreshEvent = EventFactory.createArrayBacked(Consumer.class, callbacks -> data -> {
		for (Consumer<T> callback : callbacks) {
			callback.accept(data);
		}
	});

	public DataStore(String name, Supplier<T> dataSupplier, Duration cacheDuration) {
		this(name, dataSupplier, cacheDuration, false);
	}

	public DataStore(String name, Supplier<T> dataSupplier, Duration cacheDuration, boolean allowStale) {
		this.name = name;
		this.prepareFunction = dataSupplier;
		this.cacheDuration = cacheDuration;
		this.allowStale = allowStale;
	}

	public Instant getLastRefresh() {
		return this.lastRefresh;
	}

	public boolean isStale() {
		return this.getLastRefresh().plus(cacheDuration).isBefore(Instant.now());
	}

	public CompletableFuture<Void> refresh(Executor executor, boolean force) {
		Lock writeLock = this.lock.writeLock();
		// always acquire write lock BEFORE checking whether we need to refresh
		writeLock.lock();

		// check whether we need to refresh at all
		if (!force && data != null && !this.isStale()) {
			try {
				return CompletableFuture.completedFuture(null);
			} finally {
				writeLock.unlock();
			}
		}

		// we need to refresh
		return CompletableFuture.supplyAsync(this.prepareFunction).thenAcceptAsync(data -> {
			try {
				this.data = data;
				this.lastRefresh = Instant.now();
				this.refreshEvent.invoker().accept(data);
			} finally {
				writeLock.unlock();
			}
		}, executor).exceptionally(t -> {
			try {
				SparkweaveLogging.getLogger().error("Failed to refresh {}", this.name, t);
				return null;
			} finally {
				writeLock.unlock();
			}
		});
	}

	public Optional<T> tryGet() {
		Lock readLock = lock.readLock();
		if(readLock.tryLock()) {
			try {
				return Optional.ofNullable(this.data);
			} finally {
				readLock.unlock();
			}
		}
		else if(this.allowStale) {
			return Optional.ofNullable(this.data);
		}
		return Optional.empty();
	}

	public T get() {
		Lock readLock = this.lock.readLock();
		boolean locked = readLock.tryLock();
		if(!locked && this.allowStale) {
			return this.data;
		}

		try {
			if(!locked) {
				readLock.lock();
			}
			return this.data;
		} finally {
			readLock.unlock();
		}
	}

	public void onRefresh(Consumer<T> callback) {
		this.refreshEvent.register(callback);
	}
}
