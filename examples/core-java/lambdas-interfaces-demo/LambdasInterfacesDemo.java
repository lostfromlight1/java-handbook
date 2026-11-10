/** Module 16 Ch.02 — lambdas and java.util.function types. */
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LambdasInterfacesDemo {
    public static void main(String[] args) {
        Predicate<String> nonBlank = s -> s != null && !s.isBlank();
        Function<String, Integer> length = String::length;
        Supplier<String> greeting = () -> "Hello";

        List<String> names = List.of("", "  ", "Ada", "Bob");
        long count = names.stream().filter(nonBlank).count();

        System.out.println("greeting=" + greeting.get());
        System.out.println("lenAda=" + length.apply("Ada"));
        System.out.println("nonBlankCount=" + count);
        System.out.println("mapped=" + names.stream().filter(nonBlank).map(String::toUpperCase).toList());
    }
}
