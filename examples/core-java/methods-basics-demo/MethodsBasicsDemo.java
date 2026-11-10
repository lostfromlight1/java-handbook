/**
 * Module 04 Ch.01 — method declaration, parameters, return, early return.
 * Compile: javac MethodsBasicsDemo.java
 * Run:     java MethodsBasicsDemo
 */
public class MethodsBasicsDemo {

    public static void main(String[] args) {
        greet("Ada");
        System.out.println("sum(3, 5) = " + sum(3, 5));
        System.out.println("describeScore(85) = " + describeScore(85));
        System.out.println("describeScore(40) = " + describeScore(40));
        System.out.println("clamp(150, 0, 100) = " + clamp(150, 0, 100));
        System.out.println("clamp(-5, 0, 100) = " + clamp(-5, 0, 100));
    }

    /** Prints a greeting; returns nothing (void). */
    static void greet(String name) {
        System.out.println("Hello, " + name + "!");
    }

    /** Returns the sum of two ints. */
    static int sum(int a, int b) {
        return a + b;
    }

    /** Guard-clause style: leave early when the score fails. */
    static String describeScore(int score) {
        if (score < 50) {
            return "failing";
        }
        return "passing";
    }

    /** Keeps value inside [min, max]. */
    static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }
}
