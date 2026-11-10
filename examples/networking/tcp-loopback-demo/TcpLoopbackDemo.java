import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/** Single-threaded echo server on loopback — teaching sockets, not production design. */
public final class TcpLoopbackDemo {

    static final int PORT = 19042;

    public static void main(String[] args) throws Exception {
        try (ServerSocket server = new ServerSocket(PORT)) {
            Thread serverThread = Thread.startVirtualThread(() -> handleOne(server));
            try (Socket client = new Socket("127.0.0.1", PORT);
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                out.println("ping");
                String line = in.readLine();
                System.out.println("echo=" + line);
            }
            serverThread.join();
            System.out.println("port=" + PORT);
        }
    }

    private static void handleOne(ServerSocket server) {
        try (Socket socket = server.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            String line = in.readLine();
            out.println("echo:" + line);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
