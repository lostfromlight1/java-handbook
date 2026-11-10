/** Module 12 Ch.04 — Maps and hashing. */
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapsHashingDemo {
    public static void main(String[] args) {
        Map<String, Integer> ages = new HashMap<>();
        ages.put("Ada", 36);
        ages.put("Lin", 45);
        ages.put("Ada", 37); // replace
        System.out.println("Ada=" + ages.get("Ada"));
        System.out.println("size=" + ages.size());
        System.out.println("containsLin=" + ages.containsKey("Lin"));

        Map<Key, String> keyed = new HashMap<>();
        keyed.put(new Key("A"), "one");
        keyed.put(new Key("A"), "two");
        System.out.println("keyedSize=" + keyed.size());
        System.out.println("keyedA=" + keyed.get(new Key("A")));
    }
}

final class Key {
    private final String id;

    Key(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Key k && Objects.equals(id, k.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
