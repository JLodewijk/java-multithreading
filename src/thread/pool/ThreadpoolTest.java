package thread.pool;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Performs a JUnit test for the Threadpool class.
 *
 * @author Jeroen Lodewijk
 */
public class ThreadpoolTest {
	/**
	 * @throws Exception
	 *             to test if Threadpool is working.
	 */
	@Test
	public void testThreadpool() throws Exception {
		// Set the number of executables that need to be performed.
		int executableCount = 10;

		// Get the number of threads.
		Threadpool threadpool = Threadpool.getNumberOfLogicalCores();

		// Set initial state of the thread counter.
		final AtomicInteger count = new AtomicInteger(0);
		Runnable r = new Runnable() {
			@Override
			public void run() {
				count.getAndIncrement();
			}
		};

		// execute each of the set executables.
		for (int i = 0; i < executableCount; i++) {
			threadpool.execute(r);
		}

		// Stop the threads and terminate existing ones.
		threadpool.stop();
		threadpool.awaitTermination();

		// Check if the Threadpool() is working properly.
		Assert.assertEquals("Executables executed should be same as executables sent to threadpool ", executableCount,
				count.get());
	}

	/**
	 * @throws Exception
	 *             to test if AwaitTermination is working.
	 */
	@Test
	public void testAwaitTermination() throws Exception {
		int executableCount = 6;

		// Create less threads than logical cores exist.
		Threadpool threadpool = Threadpool.getInstance(executableCount / 2);

		final AtomicInteger count = new AtomicInteger(0);
		Runnable r = new Runnable() {
			@Override
			public void run() {
				count.getAndIncrement();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		};
		for (int i = 0; i < executableCount; i++) {
			threadpool.execute(r);
		}
		threadpool.stop();
		threadpool.awaitTermination();
		Assert.assertEquals("Not all executables were await termination ", executableCount, count.get());
	}

}
