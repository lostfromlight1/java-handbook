/** Module 13 Ch.02 — bounds and wildcards (PECS). */
import java.util.ArrayList;
import java.util.List;

public class GenericsBoundsDemo {
    public static void main(String[] args) {
        List<Integer> ints = new ArrayList<>(List.of(1, 2, 3));
        System.out.println("sum=" + sum(ints));

        List<Number> nums = new ArrayList<>();
        copy(ints, nums);
        System.out.println("nums=" + nums);

        List<? extends Number> producer = ints;
        System.out.println("producer0=" + producer.get(0));
    }

    static double sum(List<? extends Number> values) {
        double total = 0;
        for (Number n : values) {
            total += n.doubleValue();
        }
        return total;
    }

    static <T> void copy(List<? extends T> src, List<? super T> dest) {
        for (T item : src) {
            dest.add(item);
        }
    }
}
