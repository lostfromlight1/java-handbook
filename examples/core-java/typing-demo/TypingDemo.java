/** Module 10 Ch.01 — static and strong typing. */
public class TypingDemo {
    public static void main(String[] args) {
        int count = 3;
        String label = "items";
        System.out.println(count + " " + label);

        Number n = Integer.valueOf(42);
        System.out.println("number=" + n.intValue());
        System.out.println("runtime=" + n.getClass().getSimpleName());

        // compile-time rejection demo is documented in chapter; runtime shows valid uses
        CharSequence cs = "hello";
        System.out.println("len=" + cs.length());
    }
}
