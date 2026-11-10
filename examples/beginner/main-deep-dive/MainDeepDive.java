/**
 * Deep dive companion: args, return from main via System.exit, and simple validation.
 * Target: Java 21
 */
public class MainDeepDive {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java MainDeepDive <name>");
            System.exit(1);
            return;
        }
        String name = args[0].trim();
        if (name.isEmpty()) {
            System.err.println("Name must not be blank");
            System.exit(2);
            return;
        }
        System.out.println("Welcome, " + name + "!");
    }
}
