/** Module 17 Ch.04 — parallel streams: ordering and pitfalls. */
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelStreamsDemo {
    public static void main(String[] args) {
        List<Integer> data = List.of(1, 2, 3, 4, 5);

        List<Integer> sequential = data.stream()
                .map(n -> n * 10)
                .toList();
        System.out.println("sequential=" + sequential);

        List<Integer> parallel = data.parallelStream()
                .map(n -> n * 10)
                .toList();
        System.out.println("parallel=" + parallel);

        AtomicInteger sideEffect = new AtomicInteger();
        List<Integer> bad = new ArrayList<>();
        // Intentionally wrong: mutating shared state from parallel stream
        try {
            data.parallelStream().forEach(n -> {
                sideEffect.incrementAndGet();
                bad.add(n);
            });
        } catch (Exception ex) {
            System.out.println("parallelError=" + ex.getClass().getSimpleName());
        }
        System.out.println("sideEffect=" + sideEffect.get());
        System.out.println("badSize=" + bad.size());
    }
}
