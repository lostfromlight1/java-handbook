/**
 * Intentionally buggy demo for the debugging chapter.
 * Goal: sum integers from 1 through n. For n = 5, correct answer is 15.
 *
 * Target: Java 21
 */
public class OffByOneBug {
    public static void main(String[] args) {
        int n = 5;
        int sum = sumUpTo(n);
        System.out.println("sumUpTo(" + n + ") = " + sum);
    }

    /** Bug: loop condition uses i < n, so n itself is never added. */
    static int sumUpTo(int n) {
        int sum = 0;
        for (int i = 1; i < n; i++) {
            sum += i;
        }
        return sum;
    }
}
