/** Module 10 Ch.02 — instanceof and pattern matching. */
public class InstanceofPatternsDemo {
    public static void main(String[] args) {
        System.out.println(describe("Ada"));
        System.out.println(describe(21));
        System.out.println(describe(null));
        System.out.println(describe(3.5));

        Object box = "JAVA";
        if (box instanceof String s && s.length() > 3) {
            System.out.println("long=" + s.toLowerCase());
        }
    }

    static String describe(Object value) {
        return switch (value) {
            case null -> "null";
            case String s -> "string:" + s;
            case Integer i -> "int:" + i;
            case Double d -> "double:" + d;
            default -> "other:" + value;
        };
    }
}
