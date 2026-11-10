/** Module 10 Ch.03 — wildcards/erasure preview (full generics Module 13). */
import java.util.ArrayList;
import java.util.List;

public class VarianceErasurePreviewDemo {
    public static void main(String[] args) {
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        System.out.println("sum=" + sum(ints));

        List<? extends Number> nums = ints;
        System.out.println("first=" + nums.get(0));

        List<Object> objs = new ArrayList<>();
        addNumber(objs, 7);
        System.out.println("objs=" + objs);

        // Erasure preview: at runtime List is raw-ish for type args
        System.out.println("listClass=" + ints.getClass().getSimpleName());
    }

    static int sum(List<? extends Number> values) {
        int total = 0;
        for (Number n : values) {
            total += n.intValue();
        }
        return total;
    }

    static void addNumber(List<? super Integer> sink, Integer value) {
        sink.add(value);
    }
}
