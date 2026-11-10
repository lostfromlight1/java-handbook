/**
 * Module 04 Ch.02 — overloading, varargs, recursion.
 * Compile: javac OverloadingDemo.java
 * Run:     java OverloadingDemo
 */
public class OverloadingDemo {

    public static void main(String[] args) {
        System.out.println("max(3, 9) = " + max(3, 9));
        System.out.println("max(3, 9, 2) = " + max(3, 9, 2));
        System.out.println("max(1.5, 2.25) = " + max(1.5, 2.25));
        System.out.println("join() = '" + join() + "'");
        System.out.println("join(\"a\") = '" + join("a") + "'");
        System.out.println("join(\"a\", \"b\", \"c\") = '" + join("a", "b", "c") + "'");
        System.out.println("factorial(5) = " + factorial(5));
        System.out.println("factorialIter(5) = " + factorialIter(5));
        System.out.println("sumTo(4) = " + sumTo(4));
    }

    static int max(int a, int b) {
        return a >= b ? a : b;
    }

    static int max(int a, int b, int c) {
        return max(max(a, b), c);
    }

    static double max(double a, double b) {
        return a >= b ? a : b;
    }

    /** Varargs: zero or more String arguments become a String[]. */
    static String join(String... parts) {
        if (parts.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            sb.append(',').append(parts[i]);
        }
        return sb.toString();
    }

    /** Recursive factorial; n must be >= 0. */
    static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        if (n <= 1) {
            return 1L;
        }
        return n * factorial(n - 1);
    }

    /** Same result without recursion (preferred for deep stacks). */
    static long factorialIter(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        long result = 1L;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    /** 1 + 2 + ... + n via recursion (teaching example). */
    static int sumTo(int n) {
        if (n <= 0) {
            return 0;
        }
        return n + sumTo(n - 1);
    }
}
