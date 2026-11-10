/** Module 14 Ch.02 — try/catch/finally and try-with-resources. */
import java.io.Closeable;

public class TryWithResourcesDemo {
    public static void main(String[] args) {
        try {
            risky(false);
            System.out.println("ok-path");
        } catch (IllegalStateException ex) {
            System.out.println("catch=" + ex.getMessage());
        } finally {
            System.out.println("finally-ran");
        }

        try {
            risky(true);
        } catch (IllegalStateException ex) {
            System.out.println("catch2=" + ex.getMessage());
        } finally {
            System.out.println("finally2-ran");
        }

        try (DemoResource r = new DemoResource("R1")) {
            System.out.println(r.read());
        } catch (Exception ex) {
            System.out.println("twr-error=" + ex.getMessage());
        }
    }

    static void risky(boolean fail) {
        if (fail) {
            throw new IllegalStateException("boom");
        }
    }
}

class DemoResource implements Closeable {
    private final String name;

    DemoResource(String name) {
        this.name = name;
        System.out.println("open=" + name);
    }

    String read() {
        return "data-" + name;
    }

    @Override
    public void close() {
        System.out.println("close=" + name);
    }
}
