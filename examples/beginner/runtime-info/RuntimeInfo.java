/**
 * Prints useful runtime facts from the running JVM.
 * Target: Java 21
 */
public class RuntimeInfo {
    public static void main(String[] args) {
        System.out.println("java.version = " + System.getProperty("java.version"));
        System.out.println("java.home = " + System.getProperty("java.home"));
        System.out.println("os.name = " + System.getProperty("os.name"));
        System.out.println("user.dir = " + System.getProperty("user.dir"));
    }
}
