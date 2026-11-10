# Solutions: Module 04 — Methods and Functions Assessment

## Part A

1. Methods name reusable units of work, reduce duplication, hide detail, and make testing/reading easier than one endless `main`.
2. **Parameters** are the declared inputs in the method signature (`int a`). **Arguments** are the values supplied at the call site (`sum(3, 5)`).
3. Overload resolution uses name + parameter types. Return type alone does not distinguish overloads (same erasure / same signature for resolution).
4. Varargs means zero or more arguments of that type, packed into an array inside the method. It must be the **last** parameter.
5. No—Java is **pass-by-value**. For `int`, the numeric bits are copied. For `int[]`, the **reference** (handle) is copied, not a deep copy of the array.
6. Mutating shared object state (`arr[0]=…`, `builder.append`) is visible. Reassigning the parameter (`arr = new …`) only changes the callee’s local copy of the reference.
7. A **stack frame** holds a call’s locals/parameters and return continuation. Each recursive call pushes another frame; unbounded depth can exhaust the thread stack → `StackOverflowError` (HotSpot limit via `-Xss`, not a JLS fixed number).
8. **Static** belongs to the type, no `this`, called as `Type.method()`. **Instance** needs a receiver object, has `this`, uses instance fields.
9. A **method reference** (`String::toUpperCase`, `Integer::sum`) is shorthand for a lambda that only calls an existing method. Lambdas implement functional interfaces; Modules 16–17 deepen this.

## Part B

10. `x` is still `5` (local copy changed only).
11. `a[0]` is `7` (same array object mutated).
12. `a[0]` is still `1` (parameter rebound; caller’s reference unchanged).
13. `max(int, int)`.
14. Empty string `""` (demo prints `join() = ''`).
15. `24` (`4*3*2*1`).

## Part C

16. Compile error: missing return statement on some path.
17. Cannot overload on return type alone.
18. Instance methods require a receiver (`counter.describe()`), not `Counter.describe()` unless `describe` is static.
19. `StackOverflowError` (or hang until stack exhausted).

## Part D

20. Example:

```java
static boolean isEven(int n) {
    return n % 2 == 0;
}
// main: print isEven(4), isEven(7) → true, false
```

21. Example:

```java
static int area(int side) { return side * side; }
static int area(int w, int h) { return w * h; }
```

22. Example:

```java
static int sum(int... values) {
    int total = 0;
    for (int v : values) total += v;
    return total;
}
```

23. Example:

```java
static long power(int base, int exp) {
    long result = 1L;
    for (int i = 0; i < exp; i++) result *= base;
    return result;
}
```

## Part E

24. `String` is immutable; `t = t + "!"` binds the parameter to a **new** string. The caller’s `s` still references `"hi"`.
25. `createdCount()` reports how many `Counter` instances were constructed (shared static). `describe()` reports that instance’s `name` and its own `ticks` (2 for the double-bumped one, 1 for the other).
26. `names.stream().map(String::toUpperCase)`.
