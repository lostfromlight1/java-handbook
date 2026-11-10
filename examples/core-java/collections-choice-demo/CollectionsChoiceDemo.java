/** Module 12 Ch.05 — choosing structures; iteration pitfalls preview. */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CollectionsChoiceDemo {
    public static void main(String[] args) {
        // random access → ArrayList
        List<Integer> scores = new ArrayList<>(List.of(1, 2, 3, 4));
        System.out.println("index2=" + scores.get(2));

        // key lookup → HashMap
        Map<String, Integer> stock = new HashMap<>();
        stock.put("kb", 5);
        stock.put("mouse", 2);
        System.out.println("kb=" + stock.getOrDefault("kb", 0));

        // safe removal via Iterator
        Iterator<Integer> it = scores.iterator();
        while (it.hasNext()) {
            if (it.next() % 2 == 0) {
                it.remove();
            }
        }
        System.out.println("odds=" + scores);
    }
}
