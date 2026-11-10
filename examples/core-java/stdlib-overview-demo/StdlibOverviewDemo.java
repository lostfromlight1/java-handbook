import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.time.Instant;

public class StdlibOverviewDemo {

    public static void main(String[] args) throws Exception {
        Path p = Path.of("notes.txt");
        System.out.println("pathFileName=" + p.getFileName());

        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putInt(42);
        System.out.println("byteBufferPos=" + buf.position());

        URI uri = URI.create("https://example.com/api");
        System.out.println("uriHost=" + uri.getHost());

        Instant now = Instant.now();
        System.out.println("instantEpoch=" + (now.getEpochSecond() > 0));
    }
}
