/**
 * Widening, narrowing casts, and local variable type inference (var).
 * Target: Java 21
 */
public class ConversionDemo {
    public static void main(String[] args) {
        int whole = 42;
        double widened = whole; // widening
        System.out.println("widened=" + widened);

        double pi = 3.99;
        int narrowed = (int) pi; // fractional part truncated
        System.out.println("narrowed=" + narrowed);

        int sumInts = 5 / 2;
        double sumMixed = 5 / 2.0;
        System.out.println("5/2 (int)=" + sumInts);
        System.out.println("5/2.0 (double)=" + sumMixed);

        var message = "inferred String";
        var total = 10 + 2;
        System.out.println("message=" + message);
        System.out.println("total=" + total);
    }
}
