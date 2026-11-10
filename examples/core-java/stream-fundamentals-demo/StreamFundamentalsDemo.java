/** Module 17 Ch.01 — stream sources, lazy pipeline setup. */
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamFundamentalsDemo {
    public static void main(String[] args) {
        List<String> names = List.of("Ada", "Lin", "Zoe");
        long size = names.stream().count();
        System.out.println("listCount=" + size);

        int sum = IntStream.rangeClosed(1, 5).sum();
        System.out.println("rangeSum=" + sum);

        Stream<String> lazy = names.stream()
                .filter(s -> {
                    System.out.println("filter:" + s);
                    return s.startsWith("L");
                });
        System.out.println("beforeTerminal");
        String first = lazy.findFirst().orElse("none");
        System.out.println("firstL=" + first);
    }
}
