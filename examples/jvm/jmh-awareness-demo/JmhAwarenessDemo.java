/**
 * Manual warm-up loop illustrating why JMH exists — not a substitute for JMH.
 */
public class JmhAwarenessDemo {

    static int work(int x) {
        return x * x + x;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5_000; i++) {
            work(i);
        }
        int sample = work(42);
        System.out.println("warmedSample=" + sample);
        System.out.println("jmhRequired=for publication-grade benchmarks");
    }
}
