/**
 * Module 05 Ch.02 — encapsulation, getters, validation, abstraction preview.
 * javac EncapsulationDemo.java && java EncapsulationDemo
 */
public class EncapsulationDemo {

    public static void main(String[] args) {
        Temperature t = Temperature.fromCelsius(25);
        System.out.println("celsius=" + t.celsius());
        System.out.println("fahrenheit=" + t.fahrenheit());

        try {
            Temperature.fromCelsius(-300);
        } catch (IllegalArgumentException ex) {
            System.out.println("rejected: " + ex.getMessage());
        }

        UserAccount user = new UserAccount("ada");
        user.setDisplayName("  Ada Lovelace  ");
        System.out.println("user=" + user.getUsername() + " display='" + user.getDisplayName() + "'");
    }
}

/** Hides representation; exposes behavior (abstraction). */
final class Temperature {
    private final double celsius;

    private Temperature(double celsius) {
        if (celsius < -273.15) {
            throw new IllegalArgumentException("below absolute zero");
        }
        this.celsius = celsius;
    }

    static Temperature fromCelsius(double c) {
        return new Temperature(c);
    }

    double celsius() {
        return celsius;
    }

    double fahrenheit() {
        return celsius * 9.0 / 5.0 + 32.0;
    }
}

class UserAccount {
    private final String username;
    private String displayName;

    UserAccount(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username required");
        }
        this.username = username;
        this.displayName = username;
    }

    String getUsername() {
        return username;
    }

    String getDisplayName() {
        return displayName;
    }

    void setDisplayName(String displayName) {
        if (displayName == null || displayName.isBlank()) {
            throw new IllegalArgumentException("displayName required");
        }
        this.displayName = displayName.trim();
    }
}
