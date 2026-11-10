/**
 * Primitive types, literals, and a String reference contrast.
 * Target: Java 21
 */
public class PrimitivesDemo {
    public static void main(String[] args) {
        byte small = 100;
        short medium = 10_000;
        int count = 42;
        long big = 3_000_000_000L;
        float ratio = 3.14f;
        double precise = 3.141592653589793;
        char letter = 'J';
        boolean ready = true;
        String label = "Java"; // reference type, not a primitive

        System.out.println("byte=" + small);
        System.out.println("short=" + medium);
        System.out.println("int=" + count);
        System.out.println("long=" + big);
        System.out.println("float=" + ratio);
        System.out.println("double=" + precise);
        System.out.println("char=" + letter);
        System.out.println("boolean=" + ready);
        System.out.println("String=" + label);
    }
}
