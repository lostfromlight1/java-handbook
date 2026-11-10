/** Module 07 Ch.01 — inheritance deep dive: constructors, protected, final methods. */
public class InheritanceDeepDemo {
    public static void main(String[] args) {
        SavingsAccount s = new SavingsAccount("S-1", 1000, 50);
        System.out.println(s.describe());
        s.deposit(200);
        System.out.println("after deposit balance=" + s.balance());
        System.out.println("canWithdraw(900)=" + s.canWithdraw(900));
        System.out.println("canWithdraw(1100)=" + s.canWithdraw(1100));
    }
}

class Account {
    private final String id;
    private int balance;

    Account(String id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    protected final String id() {
        return id;
    }

    protected int balance() {
        return balance;
    }

    protected void setBalance(int balance) {
        this.balance = balance;
    }

    void deposit(int amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    boolean canWithdraw(int amount) {
        return amount > 0 && amount <= balance;
    }

    String describe() {
        return "Account(" + id + ") bal=" + balance;
    }
}

class SavingsAccount extends Account {
    private final int minBalance;

    SavingsAccount(String id, int balance, int minBalance) {
        super(id, balance);
        this.minBalance = minBalance;
    }

    /** Per-transaction cap (business rule) plus minimum-balance invariant. */
    @Override
    boolean canWithdraw(int amount) {
        return amount > 0
                && amount <= 1_000
                && balance() - amount >= minBalance;
    }

    @Override
    String describe() {
        return "Savings(" + id() + ") bal=" + balance() + " min=" + minBalance;
    }
}
