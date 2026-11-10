/** Module 16 Ch.03 — method references types and composition. */
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class MethodReferencesDemo {
    public static void main(String[] args) {
        Function<String, String> trim = String::trim;
        UnaryOperator<String> upper = String::toUpperCase;
        Function<String, String> normalize = trim.andThen(upper);

        List<String> raw = List.of("  ada ", " LIN ");
        System.out.println("normalized=" + raw.stream().map(normalize).sorted(Comparator.naturalOrder()).toList());

        Function<Integer, Integer> times3 = x -> x * 3;
        Function<Integer, Integer> plus2 = x -> x + 2;
        Function<Integer, Integer> pipeline = times3.andThen(plus2);
        System.out.println("pipeline=" + pipeline.apply(4));
        System.out.println("compose=" + plus2.compose(times3).apply(4));
    }
}
