/** Module 13 Ch.04 — generic API design. */
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GenericsApiDemo {
    public static void main(String[] args) {
        Repository<User> users = new Repository<>();
        users.save("1", new User("Ada"));
        System.out.println("found=" + users.find("1").map(User::name).orElse("missing"));
        System.out.println("missing=" + users.find("9").isPresent());
        System.out.println("count=" + users.count());
    }
}

record User(String name) {}

class Repository<T> {
    private final Map<String, T> store = new HashMap<>();

    void save(String id, T value) {
        store.put(id, value);
    }

    Optional<T> find(String id) {
        return Optional.ofNullable(store.get(id));
    }

    int count() {
        return store.size();
    }
}
