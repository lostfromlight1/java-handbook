import java.time.Duration;

public final class CircuitBreakerDemo {
    public static void main(String[] args) throws Exception {
        CircuitBreaker breaker = new CircuitBreaker(2, Duration.ofMillis(50));
        FlakyDependency dep = new FlakyDependency();

        for (int i = 0; i < 5; i++) {
            try {
                String value = breaker.call(dep::ping);
                System.out.println("ok state=" + breaker.state() + " value=" + value);
            } catch (CircuitBreaker.CircuitOpenException ex) {
                System.out.println("fast state=" + breaker.state() + " reason=" + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("fail state=" + breaker.state());
            }
            Thread.sleep(30);
        }
        System.out.println("circuit-breaker=ok");
    }

    static final class FlakyDependency {
        private int calls;

        String ping() {
            calls++;
            if (calls <= 3) {
                throw new IllegalStateException("down");
            }
            return "pong";
        }
    }
}
