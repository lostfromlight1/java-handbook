import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonesFormattingDemo {

    public static void main(String[] args) {
        ZoneId utc = ZoneId.of("UTC");
        ZoneId yangon = ZoneId.of("Asia/Yangon");

        ZonedDateTime utcTime = ZonedDateTime.of(2026, 11, 10, 15, 30, 0, 0, utc);
        ZonedDateTime local = utcTime.withZoneSameInstant(yangon);
        System.out.println("utc=" + utcTime.toLocalTime());
        System.out.println("yangon=" + local.toLocalTime());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");
        System.out.println("formatted=" + formatter.format(local));

        System.out.println("legacyDateAvoided=true");
    }
}
