/** Module 14 Ch.03 — custom exceptions and design. */
public class CustomExceptionsDemo {
    public static void main(String[] args) {
        AccountService svc = new AccountService(100);
        try {
            svc.withdraw(40);
            System.out.println("balance=" + svc.balance());
            svc.withdraw(100);
        } catch (InsufficientFundsException ex) {
            System.out.println("error=" + ex.getMessage());
            System.out.println("shortfall=" + ex.shortfall());
        }
    }
}

class InsufficientFundsException extends Exception {
    private final int shortfall;

    InsufficientFundsException(int shortfall) {
        super("need " + shortfall + " more");
        this.shortfall = shortfall;
    }

    int shortfall() {
        return shortfall;
    }
}

class AccountService {
    private int balance;

    AccountService(int balance) {
        this.balance = balance;
    }

    int balance() {
        return balance;
    }

    void withdraw(int amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException(amount - balance);
        }
        balance -= amount;
    }
}
