import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

/** Minimal circuit breaker: CLOSED -> OPEN after failures -> HALF_OPEN probe. */
final class CircuitBreaker {
    enum State { CLOSED, OPEN, HALF_OPEN }

    private final int failureThreshold;
    private final Duration openDuration;
    private final AtomicInteger failures = new AtomicInteger();
    private volatile State state = State.CLOSED;
    private volatile Instant openedAt = Instant.EPOCH;

    CircuitBreaker(int failureThreshold, Duration openDuration) {
        this.failureThreshold = failureThreshold;
        this.openDuration = openDuration;
    }

    State state() {
        if (state == State.OPEN && Instant.now().isAfter(openedAt.plus(openDuration))) {
            state = State.HALF_OPEN;
        }
        return state;
    }

    <T> T call(RiskyCall<T> call) throws Exception {
        State s = state();
        if (s == State.OPEN) {
            throw new CircuitOpenException("fail-fast");
        }
        try {
            T result = call.run();
            onSuccess();
            return result;
        } catch (Exception ex) {
            onFailure();
            throw ex;
        }
    }

    private void onSuccess() {
        failures.set(0);
        state = State.CLOSED;
    }

    private void onFailure() {
        if (failures.incrementAndGet() >= failureThreshold) {
            state = State.OPEN;
            openedAt = Instant.now();
        }
    }

    @FunctionalInterface
    interface RiskyCall<T> {
        T run() throws Exception;
    }

    static final class CircuitOpenException extends RuntimeException {
        CircuitOpenException(String msg) {
            super(msg);
        }
    }
}
