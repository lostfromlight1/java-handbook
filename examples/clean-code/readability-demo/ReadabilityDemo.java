import java.math.BigDecimal;
import java.util.Objects;

/** Small types with clear names and single responsibility (clean code sketch). */
public final class ReadabilityDemo {
    public static void main(String[] args) {
        Money total = new Money(new BigDecimal("19.99"));
        InvoiceLine line = new InvoiceLine("SKU-1", 2, new Money(new BigDecimal("9.995")));
        Money lineTotal = line.lineTotal();
        System.out.println("total=" + total);
        System.out.println("lineTotal=" + lineTotal);
        System.out.println("readability=ok");
    }
}

final class Money {
    private final BigDecimal amount;

    Money(BigDecimal amount) {
        this.amount = Objects.requireNonNull(amount);
    }

    Money add(Money other) {
        return new Money(amount.add(other.amount));
    }

    Money multiply(int qty) {
        return new Money(amount.multiply(BigDecimal.valueOf(qty)));
    }

    @Override
    public String toString() {
        return amount.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }
}

final class InvoiceLine {
    private final String sku;
    private final int quantity;
    private final Money unitPrice;

    InvoiceLine(String sku, int quantity, Money unitPrice) {
        this.sku = sku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    Money lineTotal() {
        return unitPrice.multiply(quantity);
    }
}
