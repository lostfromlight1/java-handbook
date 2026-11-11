# Java Quick Reference (Java 21)

> **Status:** Completed  
> **Profile:** Technical reference (Levels 2–3 where space allows)  
> **Purpose:** Syntax and API cheat sheet for revision—not a substitute for the curriculum chapters.  
> **Last updated:** 2026-11-11  
> **Language level:** Java 21 LTS

Use this page when you already learned a topic and need a compact reminder. Every section links to the chapter that teaches the *why*. Snippets here are **Illustrative** unless a linked demo says otherwise.

## How to use this sheet

1. Skim the table of contents mentally: types → operators → control flow → methods/classes → modern types → collections/streams → exceptions → concurrency → modules.
2. If a line is unfamiliar, follow the chapter link before memorizing syntax.
3. Prefer `--release 21` when checking examples against this handbook’s target.

## Primitives and wrappers

Java has eight **primitive** types (values, not objects) and corresponding **wrapper** classes (objects).

| Primitive | Bits (typical) | Wrapper | Default (fields) |
|-----------|----------------|---------|------------------|
| `byte` | 8 | `Byte` | `0` |
| `short` | 16 | `Short` | `0` |
| `int` | 32 | `Integer` | `0` |
| `long` | 64 | `Long` | `0L` |
| `float` | 32 | `Float` | `0.0f` |
| `double` | 64 | `Double` | `0.0d` |
| `char` | 16 (UTF-16 code unit) | `Character` | `'\u0000'` |
| `boolean` | JVM-dependent | `Boolean` | `false` |

**Local variables** have no default; you must assign before use. **Reference types** default to `null` as fields.

```java
int count = 42;
long big = 42L;
double ratio = 0.5;
boolean ok = true;
Integer boxed = count;      // autoboxing
int unboxed = boxed;        // unboxing — NPE if boxed is null
```

**Depth:** [02-java-fundamentals/01-variables-and-types.md](../02-java-fundamentals/01-variables-and-types.md), [04-type-conversion-and-inference.md](../02-java-fundamentals/04-type-conversion-and-inference.md)

## Operators (precedence pointer)

Higher rows bind more tightly. When unsure, use parentheses—clarity beats memorizing every edge.

| Precedence (high → low) | Operators (representative) |
|-------------------------|----------------------------|
| Postfix | `expr++` `expr--` |
| Unary | `++expr` `--expr` `+` `-` `~` `!` |
| Multiplicative | `*` `/` `%` |
| Additive | `+` `-` |
| Shift | `<<` `>>` `>>>` |
| Relational | `<` `>` `<=` `>=` `instanceof` |
| Equality | `==` `!=` |
| Bitwise AND / XOR / OR | `&` `^` `|` |
| Logical AND / OR | `&&` `||` |
| Ternary | `? :` |
| Assignment | `=` `+=` `-=` `*=` … |

**Notes worth remembering:**

- `&&` / `||` short-circuit; `&` / `|` on booleans do not.
- `==` on references compares identity; use `equals` for value equality on objects.
- Integer division truncates toward zero (`5 / 2 == 2`).

**Depth:** [02-java-fundamentals/02-operators-and-expressions.md](../02-java-fundamentals/02-operators-and-expressions.md)

## Control flow

```java
if (score >= 90) { /* ... */ }
else if (score >= 60) { /* ... */ }
else { /* ... */ }

switch (day) {                    // classic statement
    case 1 -> System.out.println("Mon");  // arrow form (no fall-through)
    case 2, 3 -> System.out.println("Tue/Wed");
    default -> System.out.println("Other");
}

int len = switch (shape) {        // switch expression — must be exhaustive
    case "circle" -> 1;
    case "square" -> 4;
    default -> 0;
};

for (int i = 0; i < n; i++) { }
for (String s : list) { }         // enhanced for
while (cond) { }
do { } while (cond);

for (Object o : items) {
    if (o == null) continue;
    if (done) break;
}
```

**Pattern `switch` (Java 21 final):**

```java
static String describe(Object o) {
    return switch (o) {
        case Integer i when i > 0 -> "positive int";
        case String s -> "str:" + s;
        case null -> "null";
        default -> "other";
    };
}
```

**Depth:** [03-control-flow/](../03-control-flow/README.md), especially branching and pattern matching.

## Methods

```java
public static int add(int a, int b) {
    return a + b;
}

public void setName(String name) {     // instance method
    this.name = Objects.requireNonNull(name);
}

@SafeVarargs
public final void items(T... values) { /* varargs is an array */ }
```

| Idea | Reminder |
|------|----------|
| Overloading | Same name, different parameter lists (not return type alone) |
| Overriding | Same signature in a subclass; use `@Override` |
| `static` | Belongs to the class; no `this` |
| Pass-by-value | Primitives copied; references copied (object identity shared) |

**Depth:** [04-methods-and-functions/](../04-methods-and-functions/README.md)

## Classes, interfaces, access

```java
public class Account {
    private final String id;
    protected BigDecimal balance;

    public Account(String id) {
        this.id = id;
        this.balance = BigDecimal.ZERO;
    }

    public String id() { return id; }
}

public interface Payable {
    Money amount();
    default String label() { return "payable"; }
    static Payable zero() { /* ... */ return null; }
}
```

| Modifier | Typical meaning |
|----------|-----------------|
| `public` | Visible everywhere |
| `protected` | Package + subclasses |
| package-private (none) | Same package |
| `private` | Enclosing top-level class |
| `final` (class) | No subclasses |
| `final` (field/method) | No reassignment / no override |
| `abstract` | Incomplete type; cannot instantiate |

**Depth:** Modules [05](../05-object-oriented-programming/README.md)–[09](../09-encapsulation-and-design/README.md)

## Records and sealed types

```java
public record Point(int x, int y) { }   // final, accessors, equals/hashCode/toString

public sealed interface Shape permits Circle, Rectangle { }
public record Circle(double r) implements Shape { }
public record Rectangle(double w, double h) implements Shape { }
```

Records are nominal tuples for immutable data. Sealed types close the set of subtypes so switches can be exhaustive.

**Depth:** [15-enums-records-and-sealed-types/](../15-enums-records-and-sealed-types/README.md)

## Collections and streams (one-liners)

```java
List<String> list = new ArrayList<>();
List<String> frozen = List.of("a", "b");          // unmodifiable
Map<String, Integer> map = new HashMap<>();
Set<String> set = new HashSet<>();

list.add("x");
map.put("k", 1);
boolean has = set.contains("x");

// Sequenced collections (Java 21)
list.getFirst();
list.getLast();
list.reversed();

long positives = nums.stream()
        .filter(n -> n > 0)
        .map(n -> n * 2)
        .count();

Map<String, Long> counts = words.stream()
        .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
```

| Reach for | When |
|-----------|------|
| `ArrayList` | Random access, append-heavy lists |
| `LinkedList` | Rarely—prefer `ArrayDeque` for queues |
| `HashMap` / `HashSet` | Unordered unique keys / elements |
| `TreeMap` / `TreeSet` | Sorted order |
| `ConcurrentHashMap` | Concurrent map access |

**Depth:** [12-collections-framework/](../12-collections-framework/README.md), [17-stream-api/](../17-stream-api/README.md)

## Exceptions

```java
try {
    Files.readString(path);
} catch (NoSuchFileException e) {
    // specific first
} catch (IOException e) {
    throw new UncheckedIOException(e);
} finally {
    // always (unless JVM abort) — prefer try-with-resources for closing
}

try (InputStream in = Files.newInputStream(path)) {
    // AutoCloseable closed automatically
}
```

| Kind | Examples | Rule of thumb |
|------|----------|---------------|
| Checked | `IOException`, `SQLException` | Must catch or declare `throws` |
| Unchecked | `RuntimeException` and subclasses | Often programming errors; still handle at boundaries |
| Errors | `OutOfMemoryError`, `StackOverflowError` | Rarely catch; fix root cause |

**Depth:** [14-exceptions-and-error-handling/](../14-exceptions-and-error-handling/README.md)

## Concurrency keywords and APIs (orientation)

| Construct | Role |
|-----------|------|
| `Thread` / `Runnable` | Basic unit of concurrent work |
| `synchronized` | Intrinsic lock on object/class |
| `volatile` | Visibility + ordering for that field (not a substitute for all atomicity) |
| `java.util.concurrent.*` | Executors, locks, atomics, concurrent collections |
| Virtual threads (21) | Lightweight threads via `Thread.ofVirtual()` / virtual-thread executors |

```java
synchronized (lock) {
    // critical section
}

try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
    exec.submit(() -> doWork());
}
```

**Depth:** [24-concurrency-and-multithreading/](../24-concurrency-and-multithreading/README.md), [25-java-memory-model/](../25-java-memory-model/README.md)

## Modules one-pager

A **module** is a named set of packages with explicit dependencies and exports (Java Platform Module System, JPMS).

```text
module com.example.app {
    requires java.sql;
    requires com.example.lib;
    exports com.example.app.api;
}
```

| Directive | Meaning |
|-----------|---------|
| `requires` | Depends on another module |
| `requires transitive` | Dependents also read that module |
| `exports` | Makes a package accessible to other modules |
| `opens` | Reflective access (narrower than exporting) |
| `provides` / `uses` | Service loader wiring |

**Unnamed module:** Classic classpath code runs as an unnamed module. Many apps still ship that way; modules matter more for the JDK itself and for strong encapsulation.

**Depth:** Module system details appear across getting-started and advanced modules; for runtime loading see [26-jvm-internals/02-class-loading-and-linking.md](../26-jvm-internals/02-class-loading-and-linking.md).

## Useful `java.lang` / `java.util` reminders

```java
Objects.requireNonNull(x, "x");
Objects.equals(a, b);                 // null-safe
Optional.ofNullable(value).orElse(fallback);
String.join(",", parts);
String.format(Locale.ROOT, "%04d", n);
```

**Depth:** [20-java-standard-library/](../20-java-standard-library/README.md) (when present in your tree), plus Module 18 for Optional.

## Related appendix pages

- [java-version-history.md](java-version-history.md) — version policy and LTS landmarks  
- [jvm-reference.md](jvm-reference.md) — runtime, GC, flags  
- [common-errors.md](common-errors.md) — failures and fixes  

## References

1. [JDK 21 API documentation](https://docs.oracle.com/en/java/javase/21/docs/api/)
2. [Java Language Specification](https://docs.oracle.com/javase/specs/) (operators, definite assignment, modules)
3. OpenJDK / Oracle JDK 21 release notes for feature finality
