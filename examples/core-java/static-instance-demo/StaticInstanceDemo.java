/**
 * Module 04 Ch.04 — static vs instance methods; method references preview.
 * Compile: javac StaticInstanceDemo.java
 * Run:     java StaticInstanceDemo
 */
import java.util.Arrays;
import java.util.List;

public class StaticInstanceDemo {

    public static void main(String[] args) {
        System.out.println("Counter.nextId() = " + Counter.nextId());
        System.out.println("Counter.nextId() = " + Counter.nextId());

        Counter a = new Counter("alpha");
        Counter b = new Counter("beta");
        a.bump();
        a.bump();
        b.bump();
        System.out.println(a.describe());
        System.out.println(b.describe());
        System.out.println("Counter.createdCount() = " + Counter.createdCount());

        List<String> names = Arrays.asList("zoe", "ada", "lin");
        names.stream()
                .map(String::toUpperCase)          // instance method reference on arbitrary object
                .sorted()
                .forEach(System.out::println);     // instance method reference on a specific object (PrintStream)

        int sum = Arrays.stream(new int[] {1, 2, 3})
                .reduce(0, Integer::sum);          // static method reference
        System.out.println("Integer::sum reduce = " + sum);
    }
}

class Counter {
    private static int next = 1;
    private static int created = 0;

    private final String name;
    private int ticks;

    Counter(String name) {
        this.name = name;
        created++;
    }

    static int nextId() {
        return next++;
    }

    static int createdCount() {
        return created;
    }

    void bump() {
        ticks++;
    }

    String describe() {
        return name + " ticks=" + ticks;
    }
}
