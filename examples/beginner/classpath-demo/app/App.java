/**
 * Tiny multi-file demo compiled with an explicit classpath / out directory.
 * Target: Java 21
 */
package app;

import util.Greeter;

public class App {
    public static void main(String[] args) {
        String name = args.length > 0 ? args[0] : "world";
        System.out.println(Greeter.greet(name));
    }
}
