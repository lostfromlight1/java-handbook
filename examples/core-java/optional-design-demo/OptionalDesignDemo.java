/** Module 18 Ch.03 — Optional as return type vs fields/parameters. */
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OptionalDesignDemo {
    private final Map<Integer, String> users = new HashMap<>();

    OptionalDesignDemo() {
        users.put(1, "Ada");
    }

    public static void main(String[] args) {
        OptionalDesignDemo repo = new OptionalDesignDemo();

        String good = repo.findUserName(1).orElse("unknown");
        String missing = repo.findUserName(99).orElse("unknown");
        System.out.println("good=" + good);
        System.out.println("missing=" + missing);

        // Bad style demonstrated only in comments; good API returns Optional
        System.out.println("hasUser1=" + repo.findUserName(1).isPresent());
    }

    Optional<String> findUserName(int id) {
        return Optional.ofNullable(users.get(id));
    }

    // Anti-pattern: Optional parameter — do not use in real APIs
    // void update(String name, Optional<String> nickname) { }

    // Anti-pattern: Optional field — prefer null or absent key in map
    // Optional<String> cache = Optional.empty();
}
