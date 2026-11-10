/** Module 15 Ch.02 — records. */
public class RecordsDemo {
    public static void main(String[] args) {
        Point p1 = new Point(3, 4);
        Point p2 = new Point(3, 4);
        System.out.println(p1);
        System.out.println("x=" + p1.x());
        System.out.println("equals=" + p1.equals(p2));
        System.out.println("hashEqual=" + (p1.hashCode() == p2.hashCode()));

        Range r = new Range(1, 5);
        System.out.println("contains3=" + r.contains(3));
        System.out.println("contains9=" + r.contains(9));
    }
}

record Point(int x, int y) {}

record Range(int start, int end) {
    Range {
        if (end < start) {
            throw new IllegalArgumentException("end < start");
        }
    }

    boolean contains(int value) {
        return value >= start && value <= end;
    }
}
