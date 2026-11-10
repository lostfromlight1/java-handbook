/** Module 07 Ch.04 — sealed classes/interfaces and exhaustive switch. */
public class SealedClassesDemo {
    public static void main(String[] args) {
        Result ok = new Ok(42);
        Result err = new Err("boom");
        System.out.println(describe(ok));
        System.out.println(describe(err));
        System.out.println("area=" + area(new Circle(2)));
    }

    static String describe(Result r) {
        return switch (r) {
            case Ok o -> "ok:" + o.value();
            case Err e -> "err:" + e.message();
        };
    }

    static double area(Shape2D s) {
        return switch (s) {
            case Circle c -> Math.PI * c.radius() * c.radius();
            case Rect r -> r.w() * r.h();
        };
    }
}

sealed interface Result permits Ok, Err {}

record Ok(int value) implements Result {}

record Err(String message) implements Result {}

sealed interface Shape2D permits Circle, Rect {}

record Circle(double radius) implements Shape2D {}

record Rect(double w, double h) implements Shape2D {}
