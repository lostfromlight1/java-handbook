public class HappensBeforeDemo {

    private static volatile boolean ready;
    private static int payload;

    public static void main(String[] args) throws InterruptedException {
        Thread reader = new Thread(() -> {
            while (!ready) {
                // spin until writer publishes
            }
            System.out.println("payload=" + payload);
        });

        reader.start();
        payload = 42;
        ready = true;
        reader.join();
        System.out.println("publishDone=true");
    }
}
