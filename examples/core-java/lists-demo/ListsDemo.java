/** Module 12 Ch.02 — Lists: ArrayList vs LinkedList usage. */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListsDemo {
    public static void main(String[] args) {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("one");
        arrayList.add("two");
        arrayList.add(1, "1.5");
        System.out.println("arrayList=" + arrayList);
        System.out.println("get1=" + arrayList.get(1));

        LinkedList<String> linked = new LinkedList<>();
        linked.addFirst("head");
        linked.addLast("tail");
        System.out.println("linked=" + linked);
        System.out.println("poll=" + linked.poll());
        System.out.println("linkedAfter=" + linked);
    }
}
