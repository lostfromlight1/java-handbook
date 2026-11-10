public class SafePublicationDemo {

    private static Holder holder;

    private static final class Holder {
        final int value;

        Holder(int value) {
            this.value = value;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread reader = new Thread(() -> {
            Holder local;
            while ((local = holder) == null) {
                // wait for publication
            }
            System.out.println("finalFieldValue=" + local.value);
        });
        reader.start();
        holder = new Holder(99);
        reader.join();
        System.out.println("published=true");
    }
}
