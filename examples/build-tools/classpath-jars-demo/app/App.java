package app;

import util.Greeter;

public final class App {
    public static void main(String[] args) {
        String name = args.length > 0 ? args[0] : "jar-user";
        System.out.println("greet=" + Greeter.greet(name));
        System.out.println("fromJar=" + (App.class.getProtectionDomain().getCodeSource() != null));
    }
}
