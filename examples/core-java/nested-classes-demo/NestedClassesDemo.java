/** Module 06 Ch.02 — static nested, inner, local, anonymous classes. */
public class NestedClassesDemo {
    public static void main(String[] args) {
        Outer.StaticNested sn = new Outer.StaticNested("static-nested");
        System.out.println(sn.label());

        Outer outer = new Outer("outer-1");
        Outer.Inner inner = outer.new Inner();
        System.out.println(inner.label());

        System.out.println(outer.localGreeting("Ada"));
        System.out.println(outer.anonymousTask().run());
    }
}

class Outer {
    private final String name;

    Outer(String name) {
        this.name = name;
    }

    static class StaticNested {
        private final String tag;

        StaticNested(String tag) {
            this.tag = tag;
        }

        String label() {
            return "StaticNested:" + tag;
        }
    }

    class Inner {
        String label() {
            return "Inner sees " + name;
        }
    }

    String localGreeting(String who) {
        class Greeter {
            String greet() {
                return "Hello " + who + " from " + name;
            }
        }
        return new Greeter().greet();
    }

    interface Task {
        String run();
    }

    Task anonymousTask() {
        return new Task() {
            @Override
            public String run() {
                return "anon from " + name;
            }
        };
    }
}
