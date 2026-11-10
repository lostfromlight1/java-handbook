/** Module 08 Ch.03 — functional interfaces and marker interfaces. */
@FunctionalInterface
interface IntOp {
    int apply(int a, int b);
}

interface Entity {
    // marker: no methods — tags a type
}

record Product(String id, int price) implements Entity {}

public class FunctionalMarkerDemo {
    public static void main(String[] args) {
        IntOp add = (a, b) -> a + b;
        IntOp mul = (a, b) -> a * b;
        System.out.println("add=" + add.apply(2, 3));
        System.out.println("mul=" + mul.apply(2, 3));
        System.out.println("sum=" + reduce(new int[] {1, 2, 3, 4}, 0, add));

        Product p = new Product("P1", 10);
        System.out.println("entity=" + (p instanceof Entity));
    }

    static int reduce(int[] values, int identity, IntOp op) {
        int acc = identity;
        for (int v : values) {
            acc = op.apply(acc, v);
        }
        return acc;
    }
}
