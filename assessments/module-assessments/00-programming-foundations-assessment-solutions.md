# Solutions: Module 00 — Programming Foundations Assessment

Do not read until you have attempted the assessment.

## Part A

1. Programming is writing precise instructions a computer can follow to solve a problem or perform a task.
2. **JVM** runs bytecode. **JDK** is the development kit (compiler + tools + runtime). **JRE** historically meant a runtime-only distribution; developers typically install a JDK. Packaging names vary by vendor/era.
3. Bytecode is the portable instruction format in `.class` files. It lets the same compiled classes run on JVMs for different operating systems (with real-world caveats).
4. An algorithm is a finite, unambiguous sequence of steps. Non-code: a cooking recipe. Programming: linear scan to find a maximum score.
5. State is the data held right now. Example: `score` starts at `0`, then becomes `1` after `score = score + 1`.
6. O(1): read `a[0]`. O(n): scan all elements for max. O(n²): compare every pair with nested loops.
7. Compile-time: missing semicolon. Runtime: `ArrayIndexOutOfBoundsException`. Logic: sum loop that compiles but returns the wrong total.

## Part B

8. Output:

```text
A
B
C
```

9. best starts 4 → sees 9 → best 9 → sees 1 → stays 9. Result **9**.

10. `i < n` skips `n` when summing `1..n` inclusive (off-by-one).

## Part C

11. Reproduce with `n=5`; compare to formula 15; try `n=1` (buggy often yields 0); inspect loop bound; fix to `i <= n`; verify both cases.
12. Likely `ArrayIndexOutOfBoundsException` when `i == names.length`. Fix: `i < names.length`.

## Part D

13. Example solution shape:

```java
public class CountPasses {
    public static void main(String[] args) {
        int[] scores = {59, 60, 100};
        System.out.println(countPasses(scores));
    }

    static int countPasses(int[] values) {
        int count = 0;
        for (int value : values) {
            if (value >= 60) {
                count++;
            }
        }
        return count;
    }
}
```

Expected print: `2`

14. Example: same structure as `FindMax` with `<` instead of `>`; print `60` for `{72, 91, 60}`.

## Part E

15. Example: HotSpot tiered JIT / specific GC defaults are implementation details—not “Java the language requires this exact JIT behavior.”
16. Reasonable choices: throw `IllegalArgumentException`, or return `OptionalInt.empty()`, or document “undefined if empty” (weaker). Prefer failing clearly for public APIs.
