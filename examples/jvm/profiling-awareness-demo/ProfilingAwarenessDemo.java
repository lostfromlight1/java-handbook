import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class ProfilingAwarenessDemo {

    public static void main(String[] args) throws InterruptedException {
        ThreadMXBean threads = ManagementFactory.getThreadMXBean();
        boolean cpuTimeSupported = threads.isThreadCpuTimeSupported();
        System.out.println("cpuTimeSupported=" + cpuTimeSupported);
        long id = Thread.currentThread().threadId();
        long cpu = threads.getThreadCpuTime(id);
        Thread.sleep(10);
        long cpuAfter = threads.getThreadCpuTime(id);
        System.out.println("cpuTimeNanos=" + cpu);
        System.out.println("cpuTimeAfterSleep=" + cpuAfter);
    }
}
