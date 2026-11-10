/** Module 13 Ch.01 — generic types and methods. */
import java.util.ArrayList;
import java.util.List;

public class GenericsBasicsDemo {
    public static void main(String[] args) {
        Box<String> box = new Box<>();
        box.set("hello");
        System.out.println("value=" + box.get());

        List<Integer> nums = List.of(1, 2, 3);
        System.out.println("first=" + first(nums));
        System.out.println("pair=" + pair("a", 1));
    }

    static <T> T first(List<T> list) {
        return list.get(0);
    }

    static <A, B> String pair(A a, B b) {
        return a + ":" + b;
    }
}

class Box<T> {
    private T value;

    void set(T value) {
        this.value = value;
    }

    T get() {
        return value;
    }
}
