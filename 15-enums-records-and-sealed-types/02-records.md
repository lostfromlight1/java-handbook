# Records

> **Status:** Completed  
> **Module:** 15 — Enums, Records, and Sealed Types  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Declare **records** and explain what the compiler generates (accessor, `equals`, `hashCode`, `toString`, constructor).
- Use **compact constructors** to validate invariants without repeating parameters.
- Contrast records with **immutable classes**, **Lombok `@Value`**, and **builders** (Module 06 Ch.03).
- Add **instance methods**, **static members**, and **`implements`** clauses on records.
- State limitations: no subclassing (except sealing), no additional instance fields outside the header.
- Use records as **DTOs**, **value objects**, and **sealed hierarchy leaves** (Ch.03).

## 2. Prerequisites

- **Module 06 Ch.03–04** — immutability, `equals`/`hashCode` contracts.
- **Module 05 Ch.02** — validation and `IllegalArgumentException`.
- **Module 07 Ch.04** — records implementing sealed interfaces (intro).
- **Module 10 Ch.02** — type patterns preview on records (Ch.03 deepens).

Brief refresher: a **record** is a special kind of **final** class whose **state is described by the record header** (`record Point(int x, int y)`). The compiler generates a **canonical constructor** assigning components to **private final** fields.

## 3. Why This Concept Exists

Immutable **data carriers** (`Point`, `Money`, `UserId`) need:

- A constructor
- Accessors
- Correct `equals`/`hashCode`/`toString`

Hand-written POJOs duplicate dozens of lines and drift out of sync. **Records** (JEP 395, final in **16**, stable in **21**) let you declare **what** is in the object; the compiler supplies the **mechanical** methods.

Records are **not** a replacement for entities with identity, lazy fields, or arbitrary inheritance — they excel at **transparent carriers of immutable data**.

## 4. Conceptual Explanation

### 4.1 Syntax

```java
record Point(int x, int y) {}
```

Equivalent generated surface (conceptually):

- `private final int x, y;`
- Canonical constructor `Point(int x, int y)`
- Accessors `x()`, `y()` — **not** `getX()` unless you add such methods
- `equals`, `hashCode`, `toString` using components

### 4.2 Accessor naming

Record components use **component name** as accessor:

```java
Point p = new Point(3, 4);
int x = p.x();  // not getX() unless you define getX()
```

This matches **nominal transparency**: `toString` prints `Point[x=3, y=4]`.

### 4.3 Compact constructor

Validate without re-listing parameters:

```java
record Range(int start, int end) {
    Range {
        if (end < start) {
            throw new IllegalArgumentException("end < start");
        }
    }
}
```

The compact constructor runs **assignments to fields after** the block completes — you validate on implicit parameters `start` and `end`.

### 4.4 Custom constructors and methods

You may add **overload** canonical alternatives and **instance methods**:

```java
record Point(int x, int y) {
    static Point origin() {
        return new Point(0, 0);
    }

    double distanceFromOrigin() {
        return Math.hypot(x, y);
    }
}
```

Static fields and methods are allowed; **additional instance fields** beyond the header are **not**.

### 4.5 Implements interfaces

```java
record Circle(double radius) implements Shape2D {
    public double area() {
        return Math.PI * radius * radius;
    }
}
```

Common pattern with **sealed** parents (Module 07, Ch.03).

### 4.6 Local records

Inside a method:

```java
void demo() {
    record Pair(String a, String b) {}
    var p = new Pair("x", "y");
}
```

Use for small scoped tuples; avoid abuse when a top-level type documents the API.

### 4.7 Restrictions

| Allowed | Not allowed |
|---------|-------------|
| `implements` interfaces | `extends` another class (except `Record`) |
| Static members, compact ctor | Extra instance fields not in header |
| Instance methods | Non-static inner classes that capture mutable outer state carelessly |

Records are implicitly **final** unless nested in a way that changes visibility rules.

### 4.8 Identity vs value semantics

`equals` compares **all components**. Two `new Point(3, 4)` instances are **equal** despite different identities — correct for value objects, wrong for entities with database IDs (use regular classes or careful modeling).

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Record component** | Named part of the record header (`x`, `y`) |
| **Canonical constructor** | Constructor matching the component list |
| **Compact constructor** | Validation-only constructor block without formal parameter list |
| **Data carrier** | Type whose main job is holding data, often immutable |

## 6. Syntax and Language Rules

```java
public record User(String id, String email) {
    public User {
        Objects.requireNonNull(id);
        Objects.requireNonNull(email);
    }

    public String display() {
        return email;
    }
}
```

- Can implement **`Serializable`**; serialization emits components (understand versioning needs).
- **Generic records** are allowed: `record Box<T>(T value) {}`.
- **`var` with records** in patterns appears in Ch.03.

## 7. Step-by-Step Examples

### 7.1 Minimal — `RecordsDemo`

[examples/core-java/records-demo/](../examples/core-java/records-demo/)

**Verified output (Temurin 21.0.12.1):**

```text
Point[x=3, y=4]
x=3
equals=true
hashEqual=true
contains3=true
contains9=false
```

- `equals=true` — value equality on components.
- `hashEqual=true` — contract: equal objects share hash code.
- `contains3` / `contains9` — custom method on `Range` with compact validation.

### 7.2 Realistic — API response DTO

```java
public record Page<T>(List<T> items, int page, int size, long totalElements) {
    public Page {
        items = List.copyOf(items); // defensive copy
    }

    public boolean hasNext() {
        return (long) (page + 1) * size < totalElements;
    }
}
```

Immutability plus **`List.copyOf`** prevents callers mutating internal list.

### 7.3 Advanced — nested record in sealed family (sketch)

```java
sealed interface Expr permits Const, Add {}
record Const(int value) implements Expr {}
record Add(Expr left, Expr right) implements Expr {}
```

Full evaluation with patterns in Ch.03 `SealedPatternsDemo`.

## 8. Real-World Use Cases

- **REST/gRPC DTOs** — request/response payloads in services.
- **Domain value objects** — `Money`, `EmailAddress`, `GeoPoint`.
- **Compiler / tooling AST nodes** — often sealed + records.
- **Configuration snapshots** — immutable config loaded at startup.

## 9. Internal Behavior

`javac` desugars a record to a **final class** extending `java.lang.Record` with:

- Component fields (private final)
- Generated `equals`/`hashCode` using `Objects.equals` / `Objects.hash`
- `toString` listing component names and values

Reflection exposes `RecordComponent` metadata (`getRecordComponents()`). At runtime the type is a normal class with generated methods — no special JVM opcode beyond ordinary method calls.

## 10. Edge Cases and Limitations

- **Mutable components** — if a record holds a `List` or array without copying, **shallow immutability** breaks: `record R(List<String> xs)` does not copy unless compact ctor does.
- **Serialization** — fields are serialized; migration needs custom forms if components change.
- **JPA entities** — records are poor **entity** mappings (identity, proxies, lazy fields); prefer classes for ORM-managed rows.
- **Subclassing** — only **sealed** hierarchies may involve records as permitted subtypes; records do not extend arbitrary classes.

## 11. Common Mistakes

### Incorrect — assuming `getX()` accessors

```java
int v = point.getX(); // compile error
```

### Corrected

```java
int v = point.x();
```

### Incorrect — mutating component after construction

```java
record Holder(List<String> names) {}
// caller: holder.names().add("x"); // mutates internal list
```

### Corrected

```java
record Holder(List<String> names) {
    Holder { names = List.copyOf(names); }
}
```

## 12. Best Practices

- Keep records **small and focused**; large flattened DTOs hurt readability — group nested records.
- Validate in **compact constructor**; fail fast with clear exceptions.
- Defensive-copy **mutable** components.
- Use records for **values**; use classes for **entities** with lifecycle and identity.
- Document whether accessors expose **internal** mutable state.

## 13. Design Trade-offs

| Approach | Pros | Cons |
|----------|------|------|
| **Record** | Less boilerplate, clear data shape | No inheritance, no extra fields |
| **Immutable class + builder** | Flexible construction steps | More code |
| **Mutable POJO** | Framework familiarity | Invariants harder to enforce |

## 14. Performance Considerations

Generated `equals`/`hashCode` are straightforward — fine for typical DTO sizes. Very large component lists in `equals` are expensive — don't use records with huge graphs for frequent equality checks without custom logic.

## 15. Security Considerations

Records in public APIs still leak data in `toString` — scrub secrets (`password`, tokens) from components or override `toString` carefully (loses default transparency — document trade-off).

## 16. Java Version and Compatibility Notes

Records **final in Java 16** (JEP 395). **Java 21** adds **record patterns** in `switch` and `instanceof` (Ch.03). No preview flag required in 21 for records themselves.

## 17. Related Language Comparisons

Kotlin **data classes**, Scala **case classes**, C# **records** (9+). Java records intentionally omit **copy-with** syntax; use constructor or helper method.

## 18. Revision and Mastery

- **Gaps:** treating records as JPA entities; forgetting compact ctor does not assign fields manually.
- **You should answer:** Why do `p1` and `p2` in `RecordsDemo` have `equals=true` but different references?
- **Advanced:** customization of `equals` is not supported — if you need partial equality, use a class.

## 19. Exercises

### Beginner

1. Predict all seven lines of `RecordsDemo`.
2. Write `record FullName(String first, String last)` with non-blank validation.

### Intermediate

3. Why does `hashEqual=true` follow from `equals=true`?
4. Add `Point` method `translate(int dx, int dy)` returning **new** `Point`.

### Advanced

5. When would you refuse a record for a `Money` type (currency, scale)?

## 20. Interview Questions

1. What does the compiler generate for a record?
2. Can a record extend another class?
3. Difference between compact and canonical constructor?

## 21. Solutions

1. See §7.1.
2. `if (first.isBlank() || last.isBlank()) throw new IllegalArgumentException(...)` in compact block.
3. `equals` contract requires equal objects to have equal hash codes.
4. `return new Point(x + dx, y + dy);`
5. When you need **normalized arithmetic**, **rounding policy**, or **currency mismatch checks** beyond simple field holding — wrap in a class with behavior.

## 22. Chapter Summary

**Records** are **immutable data classes** with compiler-generated accessors and equality. **Compact constructors** validate without boilerplate. **`RecordsDemo`** shows transparency, equality, and custom methods. Records pair naturally with **sealed types** for expressive domain models — next chapter.

## 23. Key Terminology

Record · component · compact constructor · canonical constructor · data carrier

## 24. References and Further Reading

- [JLS 8.10 — Record Classes](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.10)
- [JEP 395: Records](https://openjdk.org/jeps/395)
- [Module 06 Ch.03 — immutability](../06-classes-and-objects/03-immutability-and-builders.md)
- [Module 07 Ch.04 — sealed + records](../07-inheritance-and-polymorphism/04-sealed-classes.md)

## 25. Next Steps

- [03-sealed-types-and-patterns.md](03-sealed-types-and-patterns.md) — ADTs, record patterns, exhaustive switches

## Review Checklist

- [x] Learning objectives covered
- [x] Depth gate met; `RecordsDemo` verified Temurin 21.0.12.1
- [x] Immutability and mutable-component caveats
- [x] Revision and Mastery, exercises, references
