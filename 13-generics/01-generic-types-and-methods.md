# Generic Types and Methods

> **Status:** Completed  
> **Module:** 13 — Generics  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical
> **Depth target:** Level 3 (type parameters, inference, generic methods)
> **Examples verified with:** Temurin JDK 21.0.12.1 (`javac 21.0.12.1`)

**Section policy:** Sections are **conditional unless marked mandatory**. Include a section when it adds meaningful learning value. Do **not** manufacture content to fill headings. For omitted sections, record the reason in the Review Checklist.

| Mandatory for most chapters | Conditional |
| --------------------------- | ----------- |
| Learning objectives | Internal behavior |
| Prerequisites | Performance |
| Why this exists | Security |
| Conceptual explanation | Language comparisons |
| Mechanism and execution flow *(or merge note)* | Advanced examples |
| Terminology | Real-world use cases |
| Examples | Design trade-offs *(strongly encouraged for Technical / Deep-dive)* |
| Common mistakes | Compatibility details |
| Summary and references | — |

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Declare **generic classes** and **generic methods** with type parameters such as `<T>`.
- Use the **diamond operator** `new Box<>()` and **target typing** for clearer construction.
- Explain why **`List<String>`** and **`List<Integer>`** are distinct **static types** even when they share one runtime class (preview of erasure—Ch.03).
- Avoid **raw types** in new code and understand why `List` without a type argument weakens checking.
- Read generic signatures in the **JDK collections** you use from Module 12.

## 2. Prerequisites

- **Module 10 Ch.01** — static types, strong typing.
- **Module 10 Ch.03** — invariance preview; this module deepens the same ideas.
- **Module 12** — `List<E>`, `Map<K,V>` in everyday use.

Brief refresher: a **type parameter** is a placeholder name (conventionally `T`, `E`, `K`, `V`) that stands in for a **type argument** at each use site (`String`, `Integer`, `User`). The compiler substitutes the argument when type-checking calls.

## 3. Why This Concept Exists

Before Java 5, libraries stored everything in **`Object`** and cast at use sites:

```java
List list = new ArrayList();
list.add("hello");
Integer n = (Integer) list.get(0);  // ClassCastException at runtime
```

Casts fail **late** and clutter every call site. **Generics** move correctness checks to **compile time**: `List<String>` rejects `list.add(42)`. One implementation (`ArrayList`) serves all element types without source duplication.

The same problem appears outside collections: caches, repositories, parsers, and DTO wrappers all need **type-safe boxes** without repeating code per type.

## 4. Conceptual Explanation

### 4.1 Generic class — type parameter on the type

```java
class Box<T> {
    private T value;
    void set(T value) { this.value = value; }
    T get() { return value; }
}
```

`T` is declared on the class. A **`Box<String>`** carries `String` references; a **`Box<Integer>`** carries `Integer` references. They are **different declared types**.

### 4.2 Instantiation and diamond

```java
Box<String> box = new Box<>();
box.set("hello");
```

**Diamond** (`<>`) lets the compiler infer `String` from the left-hand type. Java 7+; always prefer it when the type is obvious.

### 4.3 Generic methods — type parameter on the method

A method can introduce its **own** type parameters even in a non-generic class:

```java
static <T> T first(List<T> list) {
    return list.get(0);
}
```

`<T>` appears **before** the return type. The compiler infers `T` from arguments—`first(List.of(1, 2))` yields `Integer`.

Multiple parameters are allowed:

```java
static <A, B> String pair(A a, B b) {
    return a + ":" + b;
}
```

### 4.4 Generic types are invariant

```java
List<Integer> ints = List.of(1, 2, 3);
// List<Number> nums = ints;  // COMPILE ERROR
```

Even though `Integer` extends `Number`, **`List<Integer>` is not a subtype of `List<Number>`**. Module 10 introduced this; **Ch.02** shows how wildcards recover flexibility.

### 4.5 Connection to collections (Module 12)

`ArrayList<String>` implements `List<String>`. Your fields and parameters should use **`List<String>`**, not `ArrayList<String>`, unless you need implementation-specific methods. The **type argument** on the interface documents element type for readers and the compiler.

## 5. Mechanism and Execution Flow

Generics are a **compile-time type discipline** layered on bytecode that still speaks mostly in **raw classes and Object**.

### 5.1 Parameterized type checking

For `Box<String> box = new Box<>(); box.set(value)`:

1. **`javac`** checks `value` is assignable to **`String`** at compile time.
2. Erased bytecode uses **`Box`** with **`Object`** (or bound) fields—see Ch.03.
3. Reading **`get()`** may insert **unchecked cast** to `String` at call sites.

### 5.2 Diamond inference (`GenericsBasicsDemo`)

1. **`new Box<>()`** on the RHS lets the compiler **infer** type arguments from the LHS (`Box<String>`).
2. **`Pair.of(a, 1)`** generic methods infer **`T`/`U`** from arguments—failure to infer yields compile error, not runtime surprise.

### 5.3 Type parameter scope

- **Class parameters** (`Box<T>`) — visible across instance API.
- **Method parameters** (`<T> T first(List<T>)`) — scoped to that method; can be **static** utilities.

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Type parameter** | Formal placeholder on a class or method (`T` in `class Box<T>`) |
| **Type argument** | Concrete type substituted at use site (`String` in `Box<String>`) |
| **Arity** | Number of type parameters (`Map<K,V>` has arity 2) |
| **Raw type** | Generic type used without type arguments (`List` instead of `List<String>`) |
| **Reifiable type** | Type whose information is fully available at runtime (`String`, `int[]`; not `List<String>`) |
| **Generic method** | Method with its own type parameters (`<T> void sort(List<T> list)`) |

## 7. Syntax and Language Rules

```java
public class GenericsBasicsDemo { /* see examples/ */ }

// Class-level parameter
class Pair<K, V> {
    private final K key;
    private final V value;
    // ...
}

// Method-level parameter — must declare <T> before return type
static <T> void swap(List<T> list, int i, int j) { /* ... */ }

// Wildcards are not type parameters — Ch.02
void print(List<?> items) { }
```

Rules:

- Type parameter names are **identifiers**—shadowing is confusing; avoid reusing class names.
- **`static`** fields and methods on a generic class **cannot** refer to the class’s type parameter `T` (each instance has different `T`; statics have one slot).
- You **cannot** use primitives as type arguments: `List<int>` is illegal; use `List<Integer>` (boxing).

## 8. Step-by-Step Examples

### 8.1 Minimal — `GenericsBasicsDemo`

Path: [examples/core-java/generics-basics-demo/](../examples/core-java/generics-basics-demo/)

```java
Box<String> box = new Box<>();
box.set("hello");
System.out.println("value=" + box.get());

List<Integer> nums = List.of(1, 2, 3);
System.out.println("first=" + first(nums));
System.out.println("pair=" + pair("a", 1));
```

**Verified output (Temurin 21.0.12.1):**

```text
value=hello
first=1
pair=a:1
```

- **`Box<String>`** ties `set`/`get` to `String`.
- **`first`** infers `T = Integer` from the list argument.
- **`pair`** binds `A = String`, `B = Integer` and stringifies with `+`.

### 8.2 Realistic — typed service field

```java
public final class SessionStore {
    private final Map<String, UserSession> byId = new HashMap<>();

    public void put(String id, UserSession session) {
        byId.put(id, session);
    }

    public Optional<UserSession> get(String id) {
        return Optional.ofNullable(byId.get(id));
    }
}
```

`Map<String, UserSession>` documents keys and values; Module 12’s `HashMap` is the implementation chosen once at construction.

### 8.3 Advanced — generic method with explicit type args

```java
List<String> names = Arrays.asList("a", "b");
// Rare: explicit type argument when inference fails
Collections.<String>sort(names);
```

Explicit `<String>` on the call is the escape hatch when the compiler cannot infer from context alone.

## 9. Real-World Use Cases

- **Repositories and DAOs** — `Repository<T>` (Ch.04) with one implementation class per aggregate or one generic class with `T` entity type.
- **HTTP clients** — `ResponseBody<User>` deserialized to the correct DTO type parameter.
- **Event buses** — `subscribe(EventType<E>, Handler<E>)` pairs event payload types with handlers.
- **JDK utilities** — `Collections.emptyList()`, `Optional.of(T)`, `Comparator.comparing(Function<? super T, ? extends U>)`.

## 10. Internal Behavior

At compile time, **`javac`** type-checks bodies and call sites against substituted types. At runtime, **type erasure** (Ch.03) removes most type-parameter detail from class files; checks you see in the debugger are still **compile-time** guarantees on source. The JLS defines erasure in [JLS §4.6](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.6) and generic declarations in [§8.1.2](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.1.2).

## 11. Edge Cases and Limitations

- **Null** is a member of every reference type argument: `List<String>` may hold `null` unless your API forbids it.
- **Type inference** can fail on complex chains—add an explicit type witness or local variable with a declared type.
- **Overloading** with the same erasure is illegal (two methods cannot differ only by type parameters after erasure).
- **Exceptions**: you cannot declare `catch (T e)` for a type parameter `T`.

## 12. Common Mistakes

### Incorrect — raw type for “simplicity”

```java
List list = new ArrayList();
list.add(1);
String s = (String) list.get(0);
```

### Corrected

```java
List<String> list = new ArrayList<>();
list.add("ok");
String s = list.get(0);
```

### Incorrect — assuming `List<Integer>` is a `List<Number>`

```java
void acceptNumbers(List<Number> nums) { }
// acceptNumbers(List.of(1, 2));  // compile error
```

### Corrected — use wildcard (Ch.02)

```java
void sumProducer(List<? extends Number> nums) { }
```

## 13. Best Practices

- Never introduce **raw types** in new APIs; suppress legacy only at integration boundaries with comments.
- Name type parameters meaningfully in public APIs: `E` element, `K` key, `V` value, `T` type, `R` result.
- Prefer **generic methods** for utilities (`first`, `max`) instead of duplicating per-type methods.
- Declare fields as **interface + type argument** (`List<OrderLine>`), construct with concrete class.

## 14. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Generic class | One implementation, many type arguments | Harder bytecode, erasure limits |
| Separate class per type | Simple runtime types | Duplication, drift between copies |
| `Object` + cast | Works on oldest Java | Runtime failures, no IDE help |
| Rich inference | Less boilerplate | Occasional explicit `<Type>` needed |

## 15. Performance Considerations

Generics are a **compile-time** feature; erased bytecode does not add per-call overhead beyond casts the compiler inserts at interoperability boundaries. **Autoboxing** (`List<Integer>`) still costs allocation—use primitive streams or specialized libraries when numeric performance dominates (outside this module’s scope).

## 16. Security Considerations

Raw collections and unchecked casts at trust boundaries (deserialization, plugin loading) can **pollute the heap** with wrong element types. Prefer typed APIs and validate external input into **known** type arguments; Ch.03 covers heap pollution warnings.

## 17. Java Version and Compatibility Notes

Generics since **Java 5**. Diamond since **Java 7**. This handbook targets **Java 21**; generics are stable final features. **Valhalla** (future) may add **primitive type parameters**; not part of Java 21 semantics.

## 18. Related Language Comparisons

C# **reifies** generic types at runtime (`typeof(List<int>)` works). Java **erases** type arguments—different trade-off for JVM compatibility. C++ **templates** are expanded per instantiation (compile-time code generation); Java has one bytecode per generic class.

## 19. Revision and Mastery

- **Gaps:** confusing type parameters on a class vs on a method; thinking erasure removes **all** checking.
- **You should answer:** Why does `GenericsBasicsDemo` print `first=1` from `List.of(1,2,3)`?
- **Advanced:** recursive bounds `Enum<E extends Enum<E>>`; intersection types `T extends Serializable & Comparable<T>` (Ch.02).

## 20. Exercises

### Beginner

1. Create `Box<Integer>`, set `42`, print `get()`.
2. Predict `pair=` line in `GenericsBasicsDemo` without running.

### Intermediate

3. Write `static <T> List<T> singletonList(T value)` returning `List.of(value)`.
4. Explain why `Box<int>` does not compile.

### Advanced

5. Without wildcards yet: why cannot a method `void addNumber(List<Number> target, Integer n)` accept `List<Integer>`?

## 21. Interview Questions

1. Difference between generic class and generic method?
2. What is a raw type and why is it discouraged?
3. Are `List<String>` and `List<?>` the same?

## 22. Solutions

1. Same pattern as demo with `Integer`.
2. `a:1` — string `"a"` concatenated with `1`.
3. One-line delegating method; return type `List<T>` matches parameter type.
4. Type arguments must be reference types; `int` is primitive—use `Integer`.
5. Invariance: `List<Integer>` might not accept arbitrary `Number` via `add`; assignment would allow breaking type safety (M10 Ch.03).

## 23. Chapter Summary

**Generic types and methods** parameterize classes and APIs so one implementation serves many types with **compile-time** checking. **`Box<T>`** and **`<T> first(List<T>)`** are the two core shapes. **`List<String>`** from Module 12 is the everyday face of the same mechanism. **`GenericsBasicsDemo`** shows a generic class, method inference, and multi-parameter methods. **Wildcards and bounds** come next; **erasure** explains runtime behavior in Ch.03.

## 24. Key Terminology

Type parameter · type argument · diamond operator · raw type · generic method · invariance

## 25. References and Further Reading

- [JLS 8.1.2 — Generic classes and type parameters](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.1.2)
- [Java Tutorial — Generic Types](https://docs.oracle.com/javase/tutorial/java/generics/types.html)
- [Module 10 Ch.03 — variance preview](../10-java-type-system/03-variance-wildcards-and-erasure-preview.md)
- [Module 12 Ch.01 — collection generics preview](../12-collections-framework/01-collection-hierarchy.md)

## 26. Next Steps

- [02-bounds-and-wildcards.md](02-bounds-and-wildcards.md) — `extends`, `super`, and **PECS**
- [03-type-erasure-and-restrictions.md](03-type-erasure-and-restrictions.md) — what the JVM actually keeps

## Review Checklist

**Mandatory Depth Review:** Technical profile; parameterized types and method type inference at **Level 3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful
- [x] First-appearance concepts explained now (not deferred away)
- [x] Terminology defined before use
- [x] Examples include sufficient context to compile and run (or labeled Illustrative)
- [x] Verification status accurately labeled (**Verified** on Temurin JDK 21.0.12.1 where demos exist)
- [x] Explanations distinguish specification guarantees from implementation details where relevant
- [x] References support important technical claims
- [x] Solutions explain reasoning, not just final answers
- [x] No artificial sections, filler, or unexplained jargon
- [x] Chapter can be understood without repeatedly consulting other chapters
- [x] Conditional omissions recorded below
- [x] Revision and Mastery included when profile requires
- [x] `PROGRESS.md` updated honestly (standards-alignment pass 2026-11-11; Temurin re-verify)

**Omissions (section → reason):**

- **Primitive type parameters (Valhalla)** — out of scope for Java 21 baseline.

**Verification note:** `GenericsBasicsDemo` (`value=hello`; `first=1`; `pair=a:1`) on Temurin JDK 21.0.12.1 (2026-11-11 re-run).
