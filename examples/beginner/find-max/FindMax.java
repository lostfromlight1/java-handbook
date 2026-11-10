/**
 * Linear scan algorithm: find the largest value in an array.
 *
 * Target: Java 21
 */
public class FindMax {
    public static void main(String[] args) {
        int[] scores = {72, 91, 85, 91, 60};
        int maximum = findMax(scores);
        System.out.println("Maximum score: " + maximum);
    }

    /**
     * Returns the largest element. Assumes values is non-empty.
     */
    static int findMax(int[] values) {
        int best = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] > best) {
                best = values[i];
            }
        }
        return best;
    }
}
