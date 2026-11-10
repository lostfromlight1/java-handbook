import java.util.concurrent.atomic.AtomicInteger;

public class SynchronizationAtomicsDemo {

    private static int unsafeCounter;
    private static final AtomicInteger safeCounter = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        int threads = 4;
        int increments = 10_000;
        runUnsafe(threads, increments);
        runAtomic(threads, increments);
        runSynchronized(threads, increments);
    }

    private static void runUnsafe(int threads, int increments) throws InterruptedException {
        unsafeCounter = 0;
        Thread[] workers = startAll(threads, increments, () -> unsafeCounter++);
        joinAll(workers);
        System.out.println("unsafeCounter=" + unsafeCounter);
    }

    private static void runAtomic(int threads, int increments) throws InterruptedException {
        safeCounter.set(0);
        Thread[] workers = startAll(threads, increments, () -> safeCounter.incrementAndGet());
        joinAll(workers);
        System.out.println("atomicCounter=" + safeCounter.get());
    }

    private static void runSynchronized(int threads, int increments) throws InterruptedException {
        final int[] lockedCounter = {0};
        Thread[] workers = startAll(threads, increments, () -> {
            synchronized (lockedCounter) {
                lockedCounter[0]++;
            }
        });
        joinAll(workers);
        System.out.println("syncCounter=" + lockedCounter[0]);
    }

    private static Thread[] startAll(int threads, int increments, Runnable incrementOnce) {
        Thread[] workers = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            workers[i] = new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    incrementOnce.run();
                }
            });
            workers[i].start();
        }
        return workers;
    }

    private static void joinAll(Thread[] workers) throws InterruptedException {
        for (Thread worker : workers) {
            worker.join();
        }
    }
}
