/**
 * Module 05 Ch.01 — class, object, fields, methods, new, identity.
 * javac OopFundamentalsDemo.java && java OopFundamentalsDemo
 */
public class OopFundamentalsDemo {

    public static void main(String[] args) {
        BankAccount alice = new BankAccount("Alice", 100);
        BankAccount bob = new BankAccount("Bob", 50);

        alice.deposit(25);
        boolean ok = alice.withdraw(40);
        System.out.println("alice withdraw ok=" + ok);
        System.out.println(alice.summary());
        System.out.println(bob.summary());

        BankAccount sameRef = alice;
        System.out.println("sameRef == alice: " + (sameRef == alice));
        System.out.println("bob == alice: " + (bob == alice));
    }
}

class BankAccount {
    private final String owner;
    private int balanceCents;

    BankAccount(String owner, int balanceCents) {
        this.owner = owner;
        this.balanceCents = balanceCents;
    }

    void deposit(int cents) {
        if (cents <= 0) {
            return;
        }
        balanceCents += cents;
    }

    boolean withdraw(int cents) {
        if (cents <= 0 || cents > balanceCents) {
            return false;
        }
        balanceCents -= cents;
        return true;
    }

    String summary() {
        return owner + " balance=" + balanceCents + " cents";
    }
}
