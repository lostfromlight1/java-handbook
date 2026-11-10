public class BytecodeDemo {

    public int add(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        BytecodeDemo demo = new BytecodeDemo();
        System.out.println("sum=" + demo.add(10, 32));
    }
}
