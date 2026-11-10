import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaRegexApiDemo {

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("(?<dept>[A-Z]+)-(?<id>\\d+)");
        Matcher matcher = pattern.matcher("ENG-42 OPS-7");

        while (matcher.find()) {
            System.out.println("match=" + matcher.group() + " dept=" + matcher.group("dept"));
        }

        String replaced = pattern.matcher("ENG-42").replaceAll("[$1#$2]");
        System.out.println("replaced=" + replaced);

        boolean partial = pattern.matcher("ENG-42-extra").matches();
        System.out.println("fullMatch=" + partial);

        Matcher anchored = pattern.matcher("ENG-42");
        System.out.println("findOnce=" + anchored.find());
        System.out.println("namedId=" + anchored.group("id"));
    }
}
