/** Module 12 Ch.03 — Sets and Queues. */
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

public class SetsQueuesDemo {
    public static void main(String[] args) {
        Set<String> hash = new HashSet<>();
        hash.add("b");
        hash.add("a");
        hash.add("b");
        System.out.println("hashSize=" + hash.size());

        Set<String> linked = new LinkedHashSet<>();
        linked.add("b");
        linked.add("a");
        System.out.println("linkedOrder=" + linked);

        Set<String> tree = new TreeSet<>();
        tree.add("b");
        tree.add("a");
        System.out.println("treeOrder=" + tree);

        Queue<String> q = new ArrayDeque<>();
        q.offer("1");
        q.offer("2");
        System.out.println("peek=" + q.peek());
        System.out.println("poll=" + q.poll());
        System.out.println("queue=" + q);
    }
}
