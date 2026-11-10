import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

public class GcFundamentalsDemo {

    public static void main(String[] args) {
        Runtime rt = Runtime.getRuntime();
        long total = rt.totalMemory();
        long free = rt.freeMemory();
        System.out.println("heapTotalBytes=" + total);
        System.out.println("heapFreeBytes=" + free);

        List<GarbageCollectorMXBean> gcs = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gc : gcs) {
            System.out.println("collector=" + gc.getName());
        }
        System.out.println("gcBeanCount=" + gcs.size());
    }
}
