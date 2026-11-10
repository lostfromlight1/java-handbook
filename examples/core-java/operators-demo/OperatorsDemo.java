/**
 * Arithmetic, relational, logical operators and precedence.
 * Target: Java 21
 */
public class OperatorsDemo {
    public static void main(String[] args) {
        int a = 10;
        int b = 3;

        System.out.println("a + b = " + (a + b));
        System.out.println("a / b = " + (a / b));
        System.out.println("a % b = " + (a % b));
        System.out.println("a > b = " + (a > b));
        System.out.println("(a > 5) && (b < 5) = " + ((a > 5) && (b < 5)));
        System.out.println("2 + 3 * 4 = " + (2 + 3 * 4));
        System.out.println("(2 + 3) * 4 = " + ((2 + 3) * 4));

        int x = 5;
        System.out.println("x++ yields " + (x++) + ", then x=" + x);
        x = 5;
        System.out.println("++x yields " + (++x) + ", then x=" + x);
    }
}
