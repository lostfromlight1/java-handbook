import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JavaLangUtilDemo {

    public static void main(String[] args) {
        String raw = "  Ada Lovelace  ";
        System.out.println("strip=" + raw.strip());
        System.out.println("isBlank=" + raw.isBlank());

        List<String> names = List.of("Bob", "Ada", "Lin");
        System.out.println("copyOf=" + names);
        System.out.println("first=" + names.getFirst());

        Map<String, Integer> scores = Map.of("Ada", 95, "Lin", 88);
        System.out.println("mapAda=" + scores.get("Ada"));

        String joined = String.join("-", names);
        System.out.println("join=" + joined);

        int hash = Objects.hash("Ada", 95);
        System.out.println("hashConsistent=" + (hash == Objects.hash("Ada", 95)));
    }
}
