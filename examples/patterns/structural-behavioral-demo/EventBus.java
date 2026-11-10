import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

final class EventBus {
    private final List<Consumer<String>> listeners = new ArrayList<>();

    void subscribe(Consumer<String> listener) {
        listeners.add(listener);
    }

    void publish(String event) {
        for (Consumer<String> listener : listeners) {
            listener.accept(event);
        }
    }

    int listenerCount() {
        return listeners.size();
    }
}
