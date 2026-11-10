/** Module 11 Ch.02 — string pool, equals, common APIs. */
public class StringsDemo {
    public static void main(String[] args) {
        String a = "java";
        String b = "java";
        String c = new String("java");

        System.out.println("a==b=" + (a == b));
        System.out.println("a==c=" + (a == c));
        System.out.println("a.equals(c)=" + a.equals(c));

        String word = "java";
        System.out.println("a=" + word);

        System.out.println("upper=" + word.toUpperCase());

        String book = "handbook";
        System.out.println("sub=" + book.substring(0, 4));
    }
}
