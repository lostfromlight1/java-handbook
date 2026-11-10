public class VerifySnippets {
    public static void main(String[] args) {
        System.out.println("MAX+1=" + (Integer.MAX_VALUE + 1));
        System.out.println("NaN to int=" + ((int) Double.NaN));
        byte a = 1, b = 2;
        int sum = a + b; // binary numeric promotion: both operands become int
        System.out.println("byte+byte promoted sum=" + sum);
        char ch = 'A';
        int code = ch;
        System.out.println("char widened to int=" + code);
        System.out.println("cast -3.9 to int=" + ((int) -3.9));
    }
}
