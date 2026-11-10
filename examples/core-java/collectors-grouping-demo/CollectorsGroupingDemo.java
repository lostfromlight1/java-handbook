/** Module 17 Ch.03 — Collectors, grouping, joining. */
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

record Person(String name, String dept) {}

public class CollectorsGroupingDemo {
    public static void main(String[] args) {
        List<Person> team = List.of(
                new Person("Ada", "eng"),
                new Person("Lin", "eng"),
                new Person("Zoe", "ops")
        );

        Map<String, List<String>> byDept = team.stream()
                .collect(Collectors.groupingBy(
                        Person::dept,
                        Collectors.mapping(Person::name, Collectors.toList())
                ));
        System.out.println("byDept=" + byDept);

        String namesCsv = team.stream()
                .map(Person::name)
                .collect(Collectors.joining(", "));
        System.out.println("csv=" + namesCsv);

        Map<String, Long> counts = team.stream()
                .collect(Collectors.groupingBy(Person::dept, Collectors.counting()));
        System.out.println("counts=" + counts);
    }
}
