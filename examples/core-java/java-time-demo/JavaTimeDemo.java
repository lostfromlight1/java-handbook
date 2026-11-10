import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class JavaTimeDemo {

    public static void main(String[] args) {
        LocalDate birth = LocalDate.of(1990, 3, 15);
        LocalDate today = LocalDate.of(2026, 11, 10);
        Period age = Period.between(birth, today);
        System.out.println("ageYears=" + age.getYears());

        LocalDateTime start = LocalDateTime.of(2026, 11, 10, 9, 0);
        LocalDateTime end = start.plusHours(2).plusMinutes(30);
        Duration meeting = Duration.between(start, end);
        System.out.println("durationMinutes=" + meeting.toMinutes());

        Instant i1 = Instant.parse("2026-11-10T09:00:00Z");
        Instant i2 = i1.plus(Duration.ofHours(1));
        System.out.println("instantPlus=" + i2);

        System.out.println("useJavaTime=true");
    }
}
