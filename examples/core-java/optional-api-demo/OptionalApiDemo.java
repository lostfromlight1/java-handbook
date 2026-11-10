/** Module 18 Ch.02 — Optional creation, map, flatMap, orElse. */
import java.util.Optional;

public class OptionalApiDemo {
    public static void main(String[] args) {
        Optional<String> present = Optional.of("Ada");
        Optional<String> empty = Optional.empty();
        Optional<String> nullable = Optional.ofNullable(null);

        System.out.println("present=" + present.get());
        System.out.println("emptyIsEmpty=" + empty.isEmpty());
        System.out.println("nullableEmpty=" + nullable.isEmpty());

        String upper = present.map(String::toUpperCase).orElse("N/A");
        System.out.println("upper=" + upper);

        Optional<String> fromEmpty = empty
                .flatMap(OptionalApiDemo::wrapPrefix)
                .or(() -> Optional.of("fallback"));
        System.out.println("fromEmpty=" + fromEmpty.orElse("missing"));

        String len = findLength("  hi  ");
        System.out.println("len=" + len);
    }

    static Optional<String> wrapPrefix(String s) {
        return Optional.of(">" + s);
    }

    static String findLength(String raw) {
        return Optional.ofNullable(raw)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::length)
                .map(Object::toString)
                .orElse("0");
    }
}
