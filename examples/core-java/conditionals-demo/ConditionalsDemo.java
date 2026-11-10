/**
 * if / else if / else decision making.
 * Target: Java 21
 */
public class ConditionalsDemo {
    public static void main(String[] args) {
        int score = args.length > 0 ? Integer.parseInt(args[0]) : 85;
        System.out.println("score=" + score);
        System.out.println("grade=" + grade(score));
        System.out.println("passing=" + (score >= 60));
    }

    static String grade(int score) {
        if (score >= 90) {
            return "A";
        } else if (score >= 80) {
            return "B";
        } else if (score >= 70) {
            return "C";
        } else if (score >= 60) {
            return "D";
        } else {
            return "F";
        }
    }
}
