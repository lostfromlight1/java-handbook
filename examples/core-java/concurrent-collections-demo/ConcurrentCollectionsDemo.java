import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentCollectionsDemo {

    public static void main(String[] args) throws InterruptedException {
        Map<String, Integer> counts = new ConcurrentHashMap<>();
        counts.put("Ada", 1);

        Thread t1 = new Thread(() -> counts.merge("Ada", 1, Integer::sum));
        Thread t2 = new Thread(() -> counts.merge("Lin", 2, Integer::sum));

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Ada=" + counts.get("Ada"));
        System.out.println("Lin=" + counts.get("Lin"));
        System.out.println("size=" + counts.size());
    }
}
