/** Module 11 Ch.01 — arrays: length, loops, sort, 2D access. */
import java.util.Arrays;

public class ArraysDemo {
    public static void main(String[] args) {
        int[] scores = {95, 85, 90};
        System.out.println("len=" + scores.length);

        int max = scores[0];
        for (int i = 1; i < scores.length; i++) {
            if (scores[i] > max) {
                max = scores[i];
            }
        }
        System.out.println("max=" + max);

        int[] copy = Arrays.copyOf(scores, scores.length);
        Arrays.sort(copy);
        System.out.println("sorted=" + Arrays.toString(copy));

        char[][] grid = {
            {'a', 'b'},
            {'c', 'd'}
        };
        System.out.println("grid01=" + grid[0][1]);
    }
}
