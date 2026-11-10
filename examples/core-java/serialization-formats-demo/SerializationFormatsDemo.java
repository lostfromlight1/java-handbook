import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

public class SerializationFormatsDemo {

    record UserDto(String name, int score) implements Serializable {}

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        UserDto original = new UserDto("Ada", 97);
        byte[] serialized;
        try (var bos = new ByteArrayOutputStream();
             var oos = new ObjectOutputStream(bos)) {
            oos.writeObject(original);
            serialized = bos.toByteArray();
        }
        System.out.println("serializedBytes=" + serialized.length);

        try (var bis = new ByteArrayInputStream(serialized);
             var ois = new ObjectInputStream(bis)) {
            UserDto restored = (UserDto) ois.readObject();
            System.out.println("deserialized=" + restored.name() + ":" + restored.score());
        }

        Properties props = new Properties();
        props.setProperty("user", "Lin");
        props.setProperty("score", "88");
        try (var bos = new ByteArrayOutputStream()) {
            props.store(bos, "handbook-demo");
            String encoded = Base64.getEncoder().encodeToString(bos.toByteArray());
            System.out.println("propertiesStored=" + (!encoded.isEmpty()));
        }

        String pseudoJson = "{\"name\":\"Zoe\",\"score\":91}";
        byte[] jsonBytes = pseudoJson.getBytes(StandardCharsets.UTF_8);
        System.out.println("jsonBytes=" + jsonBytes.length);
    }
}
