/** Module 08 Ch.01 — interface fundamentals: contract, implements, multiple interfaces. */
public class InterfacesFundamentalsDemo {
    public static void main(String[] args) {
        PaymentProcessor card = new CardProcessor();
        PaymentProcessor wallet = new WalletProcessor();
        System.out.println(card.pay(1500));
        System.out.println(wallet.pay(900));
        Notifier n = new EmailNotifier();
        n.notify("paid");
        Auditable a = new CardProcessor();
        System.out.println("audit=" + a.auditId());
    }
}

interface PaymentProcessor {
    String pay(int cents);
}

interface Notifier {
    void notify(String message);
}

interface Auditable {
    String auditId();
}

class CardProcessor implements PaymentProcessor, Auditable {
    @Override
    public String pay(int cents) {
        return "card:" + cents;
    }

    @Override
    public String auditId() {
        return "CARD-1";
    }
}

class WalletProcessor implements PaymentProcessor {
    @Override
    public String pay(int cents) {
        return "wallet:" + cents;
    }
}

class EmailNotifier implements Notifier {
    @Override
    public void notify(String message) {
        System.out.println("email:" + message);
    }
}
