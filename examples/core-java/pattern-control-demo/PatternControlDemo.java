/**
 * Pattern matching for instanceof and switch (Java 21).
 * Target: Java 21
 */
public class PatternControlDemo {
    public static void main(String[] args) {
        Object[] values = { "Java", 42, 3.5, null };
        for (Object value : values) {
            System.out.println(describe(value));
        }

        System.out.println("traffic=" + trafficLight("GREEN"));
        System.out.println("shape=" + area(new Circle(2)));
        System.out.println("shape=" + area(new Rectangle(3, 4)));
    }

    static String describe(Object value) {
        if (value instanceof String s) {
            return "string len=" + s.length();
        } else if (value instanceof Integer i) {
            return "int=" + i;
        } else if (value instanceof Double d && d > 0) {
            return "positive-double=" + d;
        } else if (value == null) {
            return "null";
        } else {
            return "other";
        }
    }

    static String trafficLight(String color) {
        return switch (color) {
            case "RED" -> "stop";
            case "YELLOW" -> "caution";
            case "GREEN" -> "go";
            case null -> "missing";
            default -> "invalid";
        };
    }

    static double area(Shape shape) {
        return switch (shape) {
            case Circle c -> Math.PI * c.radius() * c.radius();
            case Rectangle r -> r.width() * r.height();
        };
    }

    sealed interface Shape permits Circle, Rectangle {}

    record Circle(double radius) implements Shape {}

    record Rectangle(double width, double height) implements Shape {}
}
