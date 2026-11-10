import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/** Key=value structured log lines without external libraries. */
public final class StructuredLoggingDemo {
    public static void main(String[] args) {
        log("INFO", "order.placed", Map.of(
                "orderId", "ord-42",
                "userId", "user-7",
                "amountCents", "1999"
        ));
        log("WARN", "payment.retry", Map.of(
                "attempt", "2",
                "reason", "timeout"
        ));
        System.out.println("structured-logging=ok");
    }

    static void log(String level, String event, Map<String, String> fields) {
        Map<String, String> all = new LinkedHashMap<>();
        all.put("ts", Instant.now().toString());
        all.put("level", level);
        all.put("event", event);
        all.putAll(fields);
        String line = all.entrySet().stream()
                .map(e -> e.getKey() + "=" + escape(e.getValue()))
                .collect(Collectors.joining(" "));
        System.out.println(line);
    }

    static String escape(String value) {
        return value.replace("\"", "\\\"");
    }
}
