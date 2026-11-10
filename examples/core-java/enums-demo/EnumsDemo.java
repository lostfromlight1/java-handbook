/** Module 15 Ch.01 — enums. */
public class EnumsDemo {
    public static void main(String[] args) {
        Level level = Level.HIGH;
        System.out.println("name=" + level.name());
        System.out.println("ordinal=" + level.ordinal());
        System.out.println("label=" + level.label());
        System.out.println("parse=" + Level.from("low"));
        System.out.println("isHigh=" + Level.HIGH.isAtLeast(Level.MEDIUM));
    }
}

enum Level {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String label;

    Level(String label) {
        this.label = label;
    }

    String label() {
        return label;
    }

    boolean isAtLeast(Level other) {
        return this.ordinal() >= other.ordinal();
    }

    static Level from(String raw) {
        return Level.valueOf(raw.trim().toUpperCase());
    }
}
