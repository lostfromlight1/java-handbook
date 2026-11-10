public class TieredCompilationDemo {

    public static int hotLoop(int iterations) {
        int sum = 0;
        for (int i = 0; i < iterations; i++) {
            sum += i;
        }
        return sum;
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        int result = 0;
        for (int run = 0; run < 20_000; run++) {
            result = hotLoop(500);
        }
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        System.out.println("result=" + result);
        System.out.println("elapsedMs=" + elapsedMs);
        System.out.println("note=Wall-clock time varies by machine; not a benchmark");
    }
}
