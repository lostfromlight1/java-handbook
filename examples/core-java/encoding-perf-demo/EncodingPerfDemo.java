/** Module 11 Ch.04 — UTF-16 chars vs UTF-8 bytes; efficient concatenation. */
import java.nio.charset.StandardCharsets;

public class EncodingPerfDemo {
    public static void main(String[] args) {
        String text = "caf\u00e9";

        System.out.println("chars=" + text.length());

        byte[] utf8 = text.getBytes(StandardCharsets.UTF_8);
        System.out.println("utf8Bytes=" + utf8.length);

        String back = new String(utf8, StandardCharsets.UTF_8);
        System.out.println("roundtripOk=" + text.equals(back));

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(i);
        }
        System.out.println("built=" + sb);
    }
}
