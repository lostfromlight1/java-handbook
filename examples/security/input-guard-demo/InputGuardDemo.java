import java.util.regex.Pattern;

/** Defensive input validation — no dangerous SQL string building. */
public final class InputGuardDemo {
    private static final Pattern USER_ID = Pattern.compile("^[a-zA-Z0-9_-]{1,64}$");

    public static void main(String[] args) {
        System.out.println("valid=" + isValidUserId("user_42"));
        System.out.println("invalid=" + isValidUserId("user;drop"));
        System.out.println("lookup=" + safeLookupUserId("user_42"));
        try {
            safeLookupUserId("../etc/passwd");
        } catch (IllegalArgumentException ex) {
            System.out.println("rejected=" + ex.getMessage());
        }
        System.out.println("input-guard=ok");
    }

    static boolean isValidUserId(String raw) {
        return raw != null && USER_ID.matcher(raw).matches();
    }

    static String safeLookupUserId(String raw) {
        if (!isValidUserId(raw)) {
            throw new IllegalArgumentException("invalid user id");
        }
        return "user:" + raw; // stand-in for parameterized query bind variable
    }
}
