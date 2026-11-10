/**
 * Block scope: variables live inside the braces that declare them.
 * Target: Java 21
 */
public class ScopeDemo {
    public static void main(String[] args) {
        int outer = 1;
        System.out.println("outer=" + outer);

        {
            int inner = 2;
            System.out.println("inner=" + inner);
            System.out.println("outer still visible=" + outer);
        }

        // inner is not visible here
        System.out.println("back to outer=" + outer);

        for (int i = 0; i < 2; i++) {
            System.out.println("loop i=" + i);
        }
        // i is not visible here
    }
}
