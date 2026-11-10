/** Module 12 Ch.01 — Collection hierarchy overview. */
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionsHierarchyDemo {
    public static void main(String[] args) {
        Collection<String> c = new ArrayList<>();
        c.add("a");
        c.add("b");
        System.out.println("collectionSize=" + c.size());

        List<String> list = List.of("x", "y");
        Set<String> set = new HashSet<>(list);
        set.add("x");
        System.out.println("listSize=" + list.size());
        System.out.println("setSize=" + set.size());
        System.out.println("containsY=" + set.contains("y"));
    }
}
