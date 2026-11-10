/** Module 09 Ch.02 — coupling and cohesion (good vs tangled). */
public class CouplingCohesionDemo {
    public static void main(String[] args) {
        OrderCalculator calc = new OrderCalculator();
        int total = calc.totalCents(2, 500, 50);
        System.out.println("total=" + total);

        InvoiceFormatter fmt = new InvoiceFormatter();
        System.out.println(fmt.format("ORD-9", total));
    }
}

/** High cohesion: only pricing rules. */
class OrderCalculator {
    int totalCents(int qty, int unitCents, int shippingCents) {
        return qty * unitCents + shippingCents;
    }
}

/** High cohesion: only presentation. Low coupling to calculator via ints. */
class InvoiceFormatter {
    String format(String id, int totalCents) {
        return id + " => " + totalCents + " cents";
    }
}
