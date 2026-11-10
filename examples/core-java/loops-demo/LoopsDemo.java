/**
 * for, enhanced for, while, do-while, break, continue.
 * Target: Java 21
 */
public class LoopsDemo {
    public static void main(String[] args) {
        System.out.print("for: ");
        for (int i = 1; i <= 3; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        int[] nums = {10, 20, 30};
        System.out.print("enhanced-for: ");
        for (int n : nums) {
            System.out.print(n + " ");
        }
        System.out.println();

        int w = 3;
        System.out.print("while: ");
        while (w > 0) {
            System.out.print(w + " ");
            w--;
        }
        System.out.println();

        int d = 0;
        System.out.print("do-while: ");
        do {
            System.out.print(d + " ");
            d++;
        } while (d < 1);
        System.out.println();

        System.out.print("continue/break: ");
        for (int i = 1; i <= 6; i++) {
            if (i % 2 == 0) {
                continue;
            }
            if (i > 4) {
                break;
            }
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
