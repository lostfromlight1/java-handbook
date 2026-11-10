import java.util.ArrayList;
import java.util.List;

public class MemoryLeakPatternDemo {

    private static final List<byte[]> LEAK = new ArrayList<>();

    public static void main(String[] args) {
        int before = LEAK.size();
        LEAK.add(new byte[1024]);
        int after = LEAK.size();
        System.out.println("leakListBefore=" + before);
        System.out.println("leakListAfter=" + after);
        System.out.println("reachable=true");
    }
}
