/** Simple test harness without JUnit — teaches Arrange-Act-Assert. */
public final class AssertDemo {

    static int sumPositive(int[] values) {
        int total = 0;
        for (int v : values) {
            if (v > 0) {
                total += v;
            }
        }
        return total;
    }

    static void assertEquals(int expected, int actual, String label) {
        if (expected != actual) {
            throw new AssertionError(label + ": expected=" + expected + " actual=" + actual);
        }
        System.out.println("pass:" + label);
    }

    public static void main(String[] args) {
        assertEquals(6, sumPositive(new int[] {1, -1, 2, 3}), "mixed");
        assertEquals(0, sumPositive(new int[] {}), "empty");
        assertEquals(0, sumPositive(new int[] {-5, -2}), "allNegative");
        System.out.println("allTests=ok");
    }
}
