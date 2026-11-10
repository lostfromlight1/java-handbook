/** Module 13 Ch.03 — type erasure and restrictions. */
import java.util.ArrayList;
import java.util.List;

public class GenericsErasureDemo {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        List<Integer> ints = new ArrayList<>();
        System.out.println("sameRawClass=" + (strings.getClass() == ints.getClass()));

        String s = identity("hi");
        Integer i = identity(7);
        System.out.println("s=" + s);
        System.out.println("i=" + i);

        // Cannot create new T[] easily — use List instead
        List<String> built = makeList("a", "b");
        System.out.println("built=" + built);
    }

    static <T> T identity(T value) {
        return value;
    }

    @SafeVarargs
    static <T> List<T> makeList(T... values) {
        List<T> list = new ArrayList<>();
        for (T v : values) {
            list.add(v);
        }
        return list;
    }
}
