/** Module 18 Ch.01 — null, NPE, defensive checks. */
public class NullNpeDemo {
    public static void main(String[] args) {
        String missing = null;
        String safe = orDefault(missing, "unknown");
        System.out.println("safe=" + safe);

        try {
            int len = missing.length();
            System.out.println("len=" + len);
        } catch (NullPointerException ex) {
            System.out.println("npe=" + ex.getClass().getSimpleName());
        }

        String user = fetchUserName(42);
        System.out.println("user=" + user);
        System.out.println("display=" + displayName(null));
    }

    static String orDefault(String value, String fallback) {
        return value == null ? fallback : value;
    }

    static String fetchUserName(int id) {
        if (id <= 0) {
            return null;
        }
        return id == 42 ? "Ada" : "guest";
    }

    static String displayName(String name) {
        if (name == null) {
            return "<anonymous>";
        }
        return name.trim();
    }
}
