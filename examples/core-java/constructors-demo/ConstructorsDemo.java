/** Module 06 Ch.01 — constructors, this(), overloading, initialization order. */
public class ConstructorsDemo {
    public static void main(String[] args) {
        User a = new User("ada");
        User b = new User("lin", "Lin");
        System.out.println(a.describe());
        System.out.println(b.describe());
        System.out.println("created=" + User.created());

        Point p = new Point(3, 4);
        System.out.println("point=" + p);
    }
}

class User {
    private static int created;
    private final String username;
    private final String displayName;

    static {
        // class initialization runs once before first use
        created = 0;
    }

    User(String username) {
        this(username, username); // constructor chaining
    }

    User(String username, String displayName) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username required");
        }
        this.username = username;
        this.displayName = displayName == null ? username : displayName;
        created++;
    }

    static int created() {
        return created;
    }

    String describe() {
        return username + "/" + displayName;
    }
}

class Point {
    private final int x;
    private final int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
