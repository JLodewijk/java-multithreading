package thread.pool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

class ThreadpoolThread extends Thread {
	private AtomicBoolean execute;
	private ConcurrentLinkedQueue<Runnable> executables;

	public ThreadpoolThread(String name, AtomicBoolean execute, ConcurrentLinkedQueue<Runnable> executables) {
		// Take the value of variable name of the base class.
		super(name);
		this.execute = execute;
		this.executables = executables;
	}

	@Override
	public void run() {
		try {
			// Keep running until execute is true or if there are any
			// executables present in the queue.
			while (execute.get() || !executables.isEmpty()) {
				Runnable runnable;
				// Retrieves executables until the queue is empty (return null).
				while ((runnable = executables.poll()) != null) {
					runnable.run();
				}
				// Sleep the thread in case of an empty queue.
				Thread.sleep(1);
			}
		} catch (RuntimeException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
