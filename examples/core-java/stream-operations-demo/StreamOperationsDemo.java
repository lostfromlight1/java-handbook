/** Module 17 Ch.02 — intermediate and terminal operations. */
import java.util.List;
import java.util.Optional;

public class StreamOperationsDemo {
    public static void main(String[] args) {
        List<Integer> nums = List.of(3, 10, 7, 4, 8);

        List<Integer> evensDouble = nums.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 2)
                .sorted()
                .toList();
        System.out.println("evensDouble=" + evensDouble);

        int total = nums.stream().reduce(0, Integer::sum);
        System.out.println("sum=" + total);

        Optional<Integer> max = nums.stream().max(Integer::compareTo);
        System.out.println("max=" + max.orElse(-1));

        boolean anyBig = nums.stream().anyMatch(n -> n > 9);
        System.out.println("anyBig=" + anyBig);
    }
}
