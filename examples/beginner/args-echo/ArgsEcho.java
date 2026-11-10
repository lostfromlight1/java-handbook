/**
 * Demonstrates command-line arguments passed to main.
 * Target: Java 21
 */
public class ArgsEcho {
    public static void main(String[] args) {
        System.out.println("Argument count: " + args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.println("args[" + i + "] = " + args[i]);
        }
        if (args.length == 0) {
            System.out.println("Tip: try java ArgsEcho hello Java 21");
        }
    }
}
