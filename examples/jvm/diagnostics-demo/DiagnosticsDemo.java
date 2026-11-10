import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class DiagnosticsDemo {

    public static void main(String[] args) {
        ThreadMXBean threads = ManagementFactory.getThreadMXBean();
        System.out.println("threadCount=" + threads.getThreadCount());
        System.out.println("peakThreadCount=" + threads.getPeakThreadCount());
        long[] ids = threads.getAllThreadIds();
        System.out.println("threadsListed=" + ids.length);
    }
}
