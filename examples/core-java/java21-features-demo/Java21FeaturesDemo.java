import java.util.ArrayList;
import java.util.SequencedCollection;
import java.util.concurrent.Executors;

public class Java21FeaturesDemo {

    record Point(int x, int y) {}

    public static void main(String[] args) throws Exception {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var future = executor.submit(() -> "vt-ready");
            System.out.println("virtualThread=" + future.get());
        }

        SequencedCollection<String> seq = new ArrayList<>();
        seq.addFirst("head");
        seq.addLast("tail");
        System.out.println("sequenced=" + seq.getFirst() + "/" + seq.getLast());

        Object shape = new Point(3, 4);
        if (shape instanceof Point(int x, int y)) {
            System.out.println("recordPattern=" + x + "+" + y + "=" + (x + y));
        }

        String level = switch (args.length) {
            case 0 -> "default";
            case 1 -> "one-arg";
            default -> "many-args";
        };
        System.out.println("switchExpr=" + level);

        var textBlock = """
                line-one
                line-two
                """;
        System.out.println("textBlockLines=" + textBlock.lines().count());
    }
}
