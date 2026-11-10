import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilesStreamsDemo {

    public static void main(String[] args) throws IOException {
        Path temp = Files.createTempFile("handbook-io-", ".txt");
        try {
            Files.writeString(temp, "alpha\nbeta\ngamma\n", StandardCharsets.UTF_8);
            long lineCount = Files.lines(temp).count();
            System.out.println("tempLines=" + lineCount);

            String inline = """
                    one
                    two
                    """;
            try (var reader = new BufferedReader(new StringReader(inline));
                 var writer = new StringWriter()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line.toUpperCase());
                    writer.write('\n');
                }
                System.out.println("bufferedTransform=" + writer.toString().strip());
            }

            byte[] bytes = Files.readAllBytes(temp);
            System.out.println("readBytes=" + bytes.length);
        } finally {
            Files.deleteIfExists(temp);
        }
    }
}
