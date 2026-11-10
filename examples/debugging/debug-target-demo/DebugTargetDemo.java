import java.util.List;

/** Intentional logic bug for debugging practice (fixed version documented in chapter). */
public final class DebugTargetDemo {

    static int countLongerThan(List<String> words, int minLength) {
        int count = 0;
        // Bug: uses > instead of >= for "at least minLength"
        for (String word : words) {
            if (word.length() > minLength) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        List<String> words = List.of("a", "ab", "abc", "abcd");
        int count = countLongerThan(words, 3);
        System.out.println("count=" + count);
        System.out.println("expectedCorrect=1");
    }
}
