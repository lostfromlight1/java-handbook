import java.util.concurrent.Executors;

public class VirtualThreadsDemo {

    public static void main(String[] args) throws Exception {
        Thread vt = Thread.startVirtualThread(() -> {
            System.out.println("isVirtual=" + Thread.currentThread().isVirtual());
            System.out.println("vtName=" + Thread.currentThread().getName());
        });
        vt.join();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> System.out.println("executorVirtual=" + Thread.currentThread().isVirtual())).get();
        }

        System.out.println("platformIsVirtual=" + Thread.currentThread().isVirtual());
    }
}
