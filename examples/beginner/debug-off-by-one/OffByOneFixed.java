/**
 * Corrected version of OffByOneBug: sum of integers from 1 through n.
 *
 * Target: Java 21
 */
public class OffByOneFixed {
    public static void main(String[] args) {
        int n = 5;
        int sum = sumUpTo(n);
        System.out.println("sumUpTo(" + n + ") = " + sum);
    }

    static int sumUpTo(int n) {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        return sum;
    }
}
