public class RegexFundamentalsDemo {

    public static void main(String[] args) {
        String email = "ada@example.com";
        String pattern = "^[\\w.-]+@[\\w.-]+\\.\\w+$";

        boolean matches = email.matches(pattern);
        System.out.println("emailValid=" + matches);

        String messy = "order-1042-shipped";
        String digits = messy.replaceAll(".*-(\\d+)-.*", "$1");
        System.out.println("capturedId=" + digits);

        String words = "alpha beta gamma";
        String[] parts = words.split("\\s+");
        System.out.println("splitCount=" + parts.length);
        System.out.println("secondWord=" + parts[1]);
    }
}
