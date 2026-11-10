/**
 * Module 04 Ch.03 — Java passes arguments by value (including reference values).
 * Compile: javac PassByValueDemo.java
 * Run:     java PassByValueDemo
 */
public class PassByValueDemo {

    public static void main(String[] args) {
        int x = 10;
        tryMutatePrimitive(x);
        System.out.println("after tryMutatePrimitive, x = " + x);

        int[] nums = {1, 2, 3};
        mutateArrayContents(nums);
        System.out.println("after mutateArrayContents, nums[0] = " + nums[0]);

        reassignArrayParameter(nums);
        System.out.println("after reassignArrayParameter, nums[0] = " + nums[0]);

        StringBuilder sb = new StringBuilder("Hi");
        appendToBuilder(sb);
        System.out.println("after appendToBuilder, sb = " + sb);

        reassignBuilder(sb);
        System.out.println("after reassignBuilder, sb = " + sb);

        String s = "hello";
        tryReassignString(s);
        System.out.println("after tryReassignString, s = " + s);
    }

    static void tryMutatePrimitive(int value) {
        value = 99; // changes only the local copy
    }

    static void mutateArrayContents(int[] arr) {
        arr[0] = 100; // same array object; contents visible to caller
    }

    static void reassignArrayParameter(int[] arr) {
        arr = new int[] {9, 9, 9}; // rebinds local parameter only
    }

    static void appendToBuilder(StringBuilder builder) {
        builder.append('!');
    }

    static void reassignBuilder(StringBuilder builder) {
        builder = new StringBuilder("NEW");
    }

    static void tryReassignString(String text) {
        text = text + "!"; // new String; caller's reference unchanged
    }
}
