/** Module 08 Ch.02 — default, static, private interface methods. */
public class InterfaceMethodsDemo {
    public static void main(String[] args) {
        Greeter g = new FriendlyGreeter();
        System.out.println(g.greet("Ada"));
        System.out.println(g.greetLoud("Ada"));
        System.out.println(Greeter.banner("team"));
    }
}

interface Greeter {
    String greet(String name);

    default String greetLoud(String name) {
        return shout(greet(name));
    }

    static String banner(String who) {
        return "*** " + who + " ***";
    }

    private String shout(String text) {
        return text.toUpperCase();
    }
}

class FriendlyGreeter implements Greeter {
    @Override
    public String greet(String name) {
        return "hello " + name;
    }
}
