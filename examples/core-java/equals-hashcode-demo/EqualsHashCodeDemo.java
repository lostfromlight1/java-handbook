/** Module 06 Ch.04 — equals, hashCode, toString contracts. */
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class EqualsHashCodeDemo {
    public static void main(String[] args) {
        Sku a = new Sku("ABC", 1);
        Sku b = new Sku("ABC", 1);
        Sku c = new Sku("XYZ", 1);

        System.out.println("a.equals(b)=" + a.equals(b));
        System.out.println("a.equals(c)=" + a.equals(c));
        System.out.println("a.hashCode()==b.hashCode()=" + (a.hashCode() == b.hashCode()));
        System.out.println("a=" + a);

        Set<Sku> set = new HashSet<>();
        set.add(a);
        set.add(b); // equal to a — should not grow
        set.add(c);
        System.out.println("setSize=" + set.size());
    }
}

final class Sku {
    private final String code;
    private final int version;

    Sku(String code, int version) {
        this.code = Objects.requireNonNull(code);
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sku other)) {
            return false;
        }
        return version == other.version && code.equals(other.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, version);
    }

    @Override
    public String toString() {
        return "Sku[" + code + "@" + version + "]";
    }
}
