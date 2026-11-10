/**
 * Classic switch statement and switch expression (Java 14+).
 * Target: Java 21
 */
public class SwitchDemo {
    public static void main(String[] args) {
        String day = args.length > 0 ? args[0] : "MON";
        System.out.println("day=" + day);
        printLegacy(day);
        System.out.println("kind=" + kind(day));
        System.out.println("label=" + label(2));
    }

    static void printLegacy(String day) {
        switch (day) {
            case "MON":
            case "TUE":
            case "WED":
            case "THU":
            case "FRI":
                System.out.println("legacy=weekday");
                break;
            case "SAT":
            case "SUN":
                System.out.println("legacy=weekend");
                break;
            default:
                System.out.println("legacy=unknown");
                break;
        }
    }

    static String kind(String day) {
        return switch (day) {
            case "MON", "TUE", "WED", "THU", "FRI" -> "weekday";
            case "SAT", "SUN" -> "weekend";
            default -> "unknown";
        };
    }

    static String label(int code) {
        return switch (code) {
            case 1 -> "one";
            case 2 -> "two";
            case 3 -> "three";
            default -> "other";
        };
    }
}
