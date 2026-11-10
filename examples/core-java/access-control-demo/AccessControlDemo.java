/** Module 09 Ch.01 — access control and API boundaries. */
public class AccessControlDemo {
    public static void main(String[] args) {
        Ledger ledger = new Ledger();
        ledger.credit(100);
        ledger.credit(50);
        System.out.println("balance=" + ledger.balance());
        System.out.println("entries=" + ledger.entryCount());
        // cannot touch private fields/methods from here
    }
}

class Ledger {
    private int balance;
    private int entries;

    public void credit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount");
        }
        apply(amount);
    }

    public int balance() {
        return balance;
    }

    public int entryCount() {
        return entries;
    }

    private void apply(int amount) {
        balance += amount;
        entries++;
    }
}
