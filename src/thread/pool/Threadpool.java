package thread.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread pool Java program to demonstrate the use of a thread pool within Java.
 * 
 * @author Jeroen Lodewijk
 */
public class Threadpool {
	// Use a FIFO thread-safe queue for storing every executable.
	private ConcurrentLinkedQueue<Runnable> executables;

	// Boolean to control the creation of threads. Use AtomicBoolean to provide
	// a thread safe handling on a single variable.
	private AtomicBoolean execute;

	// Stores every thread within a list.
	private List<ThreadpoolThread> threads;

	/**
	 * Constructor to provide a control for the creation of thread pools. Also
	 * keeps track of the number of threads created for the thread pool.
	 *
	 * @param threadCount
	 *            int Determines the number of threads created for the thread
	 *            pool.
	 */
	private Threadpool(int threadCount) {
		this.executables = new ConcurrentLinkedQueue<Runnable>();
		this.execute = new AtomicBoolean(true);
		this.threads = new ArrayList<ThreadpoolThread>();

		// Create the threads for the thread pool.
		for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
			// Create a new SimpleThreadpoolThread object within a unique thread
			// ID.
			ThreadpoolThread thread = new ThreadpoolThread("Thread" + threadIndex, this.execute, this.executables);
			thread.start();
			this.threads.add(thread);
		}
	}

	/**
	 * Gets the number of logical cores on a CPU, than uses this number to
	 * create the same number of threads in getInstance()
	 *
	 * @return new SimpleThreadpool(threadCount) is an object calls the
	 *         constructor with the threadCount.
	 */
	public static Threadpool getNumberOfLogicalCores() {
		return Threadpool.getInstance(Runtime.getRuntime().availableProcessors());
	}

	/**
	 * Gets a new thread pool object that has a certain number of threads.
	 *
	 * @param threadCount
	 *            an int indicates the number of threads to add to the thread
	 *            pool.
	 * 
	 * @return new SimpleThreadpool object with the number of threads based on
	 *         threadCount.
	 */
	public static Threadpool getInstance(int threadCount) {
		return new Threadpool(threadCount);
	}

	/**
	 * Adds an executable to the ConcurrentLinkedQueue for processing.
	 *
	 * @param runnable
	 *            executable task to be added to the queue.
	 */
	public void execute(Runnable executable) {
		executables.add(executable);
	}

	/**
	 * Awaits the termination of all the threads in the thread pool
	 * indefinitely.
	 *
	 */
	public void awaitTermination() {
		while (true) {
			boolean threadsAlive = false;
			// Iterate through every thread
			for (Thread thread : threads) {
				// Check if the thread is alive, if so mark it for termination.
				if (thread.isAlive()) {
					threadsAlive = true;
					break;
				}
			}
			// Exit of the while loop by checking if there are any threads
			// alive.
			if (threadsAlive == false) {
				break;
			}
		}
	}

	/**
	 * Empties the queue of any executables and stops the thread pool. Will not
	 * stop any of the ongoing executables.
	 */
	public void terminate() {
		this.executables.clear();
		stop();
	}

	/**
	 * Prevents any new executables to be added to the thread pool and stops the
	 * thread pool once all executables in the queue are executed.
	 */
	public void stop() {
		this.execute.set(false);
	}
}
