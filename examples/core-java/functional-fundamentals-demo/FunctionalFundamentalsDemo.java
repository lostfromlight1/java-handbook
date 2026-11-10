/** Module 16 Ch.01 — first-class behavior, higher-order functions, closures. */
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntUnaryOperator;

public class FunctionalFundamentalsDemo {
    public static void main(String[] args) {
        int base = 10;
        IntUnaryOperator addBase = x -> x + base; // closure over effectively final base

        System.out.println("apply=" + addBase.applyAsInt(5));
        System.out.println("sorted=" + sortByLength(List.of("fig", "apple", "kiwi")));
        System.out.println("transformed=" + map(List.of(1, 2, 3), x -> x * 10));
    }

    static List<String> sortByLength(List<String> words) {
        List<String> copy = new ArrayList<>(words);
        copy.sort(Comparator.comparingInt(String::length)); // behavior as data
        return copy;
    }

    static <T, R> List<R> map(List<T> items, java.util.function.Function<T, R> fn) {
        List<R> out = new ArrayList<>();
        for (T item : items) {
            out.add(fn.apply(item));
        }
        return out;
    }
}
