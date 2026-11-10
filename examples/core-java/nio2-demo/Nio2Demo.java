import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Nio2Demo {

    public static void main(String[] args) throws IOException {
        Path dir = Files.createTempDirectory("handbook-nio2-");
        try {
            Path file = dir.resolve("data.txt");
            Files.writeString(file, "nio2-line", StandardCharsets.UTF_8);

            System.out.println("exists=" + Files.exists(file));
            System.out.println("size=" + Files.size(file));
            System.out.println("read=" + Files.readString(file));

            Path sibling = dir.resolve("copy.txt");
            Files.copy(file, sibling);
            List<Path> entries = Files.list(dir).sorted().toList();
            System.out.println("listCount=" + entries.size());
        } finally {
            try (var walk = Files.walk(dir)) {
                walk.sorted((a, b) -> -a.compareTo(b))
                        .forEach(p -> {
                            try {
                                Files.deleteIfExists(p);
                            } catch (IOException ignored) {
                            }
                        });
            }
        }
    }
}
