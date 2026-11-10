public class RuntimeDataAreasDemo {

    private static int callDepth(int n) {
        if (n <= 0) {
            return 0;
        }
        return 1 + callDepth(n - 1);
    }

    public static void main(String[] args) {
        Object onHeap = new Object();
        int depth = callDepth(100);
        System.out.println("stackDepth=" + depth);
        System.out.println("heapObjectId=" + System.identityHashCode(onHeap));
    }
}
