import java.util.concurrent.StructuredTaskScope;

@SuppressWarnings("preview")
public class PreviewIncubatorDemo {

    public static void main(String[] args) throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var handle = scope.fork(() -> "scoped-result");
            scope.join();
            scope.throwIfFailed();
            System.out.println("structuredConcurrency=" + handle.get());
        }
        System.out.println("compileFlags=--enable-preview --release 21");
        System.out.println("status=Structured Concurrency is preview in Java 21; check JEP for current phase");
    }
}
