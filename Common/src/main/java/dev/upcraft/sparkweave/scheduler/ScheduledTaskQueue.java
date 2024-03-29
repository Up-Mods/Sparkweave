package dev.upcraft.sparkweave.scheduler;

import com.mojang.datafixers.util.Either;
import dev.upcraft.sparkweave.api.scheduler.Task;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.LongSupplier;

public class ScheduledTaskQueue {

	private static final List<AbstractTask<?>> TASK_QUEUE = new ArrayList<>(16);
	private static volatile LongSupplier timeSupplier = () -> 0;

	public static <T> CompletableFuture<Either<T, ? extends Exception>> scheduleEphemeral(Callable<T> task, long delayTicks) {
		var scheduledTask = new SingleRunTask<>(timeSupplier.getAsLong() + Math.max(delayTicks, 0), task);
		TASK_QUEUE.add(scheduledTask);
		return scheduledTask.result();
	}

	public static <T> Task<T> scheduleEphemeralAtFixedRate(Callable<T> task, long delayTicks, long periodTicks) {
		var scheduledTask = new RepeatingTask<>(timeSupplier.getAsLong() + Math.max(delayTicks, 0), periodTicks, task);
		TASK_QUEUE.add(scheduledTask);
		return scheduledTask;
	}

	public static void onServerStarting(MinecraftServer server) {
		TASK_QUEUE.clear();
		timeSupplier = server.getWorldData().overworldData()::getGameTime;
	}

	public static void onServerStopped() {
		// set all tasks to a cancelled state before clearing the queue
		TASK_QUEUE.forEach(AbstractTask::cancel);
		TASK_QUEUE.clear();
		timeSupplier = () -> 0;
	}

	public static void onServerTick() {
		if (TASK_QUEUE.isEmpty()) return;

		long time = timeSupplier.getAsLong();

		// use a for-i loop to allow for editing the list while iterating
		for (int i = 0; i < TASK_QUEUE.size(); i++) {
			AbstractTask<?> task = TASK_QUEUE.get(i);

			if (task.isCancelled()) {
				TASK_QUEUE.remove(i--);
				continue;
			}

			if (task.nextExecutionTime() <= time) {
				if (!task.run(timeSupplier)) {
					TASK_QUEUE.remove(i--);
				}
			}
		}
	}
}
