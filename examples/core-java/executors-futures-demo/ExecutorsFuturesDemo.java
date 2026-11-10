import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ExecutorsFuturesDemo {

    public static void main(String[] args) throws Exception {
        try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
            Future<Integer> future = pool.submit(new Callable<Integer>() {
                @Override
                public Integer call() {
                    return 21 + 21;
                }
            });
            Integer result = future.get(5, TimeUnit.SECONDS);
            System.out.println("futureResult=" + result);
            System.out.println("done=" + future.isDone());
        }
        System.out.println("poolShutdown=ok");
    }
}
