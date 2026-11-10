/**
 * Simple data held in parallel arrays — a stepping stone before objects and collections.
 *
 * Target: Java 21
 */
public class Scoreboard {
    public static void main(String[] args) {
        String[] names = {"Ada", "Grace", "Alan"};
        int[] scores = {95, 88, 92};

        System.out.println("Scoreboard:");
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i] + " -> " + scores[i]);
        }

        int total = 0;
        for (int score : scores) {
            total += score;
        }
        double average = (double) total / scores.length;
        System.out.println("Average: " + average);
    }
}
