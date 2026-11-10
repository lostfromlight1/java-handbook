/**
 * Demonstrates that statements run in order and that a method call
 * pauses the caller until the method returns.
 *
 * Target: Java 21
 */
public class ExecutionOrder {
    public static void main(String[] args) {
        System.out.println("1. main starts");
        greet("Ada");
        System.out.println("3. main continues after greet returns");
        int total = add(2, 3);
        System.out.println("5. add returned: " + total);
        System.out.println("6. main ends");
    }

    static void greet(String name) {
        System.out.println("2. inside greet: Hello, " + name);
    }

    static int add(int a, int b) {
        System.out.println("4. inside add");
        return a + b;
    }
}
