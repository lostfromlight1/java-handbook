# Generic API Design

> **Status:** Completed  
> **Module:** 13 — Generics  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Design **type-safe repositories** and caches with **`Repository<T>`** and **`Map<K,V>`** without raw types.
- Return **`Optional<T>`** instead of **null** from lookups.
- Choose between **generic class** vs **generic methods** in public APIs.
- Apply **PECS** at module boundaries when methods only read or only write collections.
- Avoid leaking **mutable internal collections** and **wildcard return types** that trap callers.

## 2. Prerequisites

- **Ch.01–03** — types, wildcards, erasure.
- **Module 12** — `HashMap`, `Map` contracts.
- **Module 09** — encapsulation and stable API surfaces.

## 3. Why This Concept Exists

Teams ship **libraries** and **services** whose signatures live for years. A repository interface typed as **`Map`**, **`Object`**, or **`List`** shifts correctness to every caller. **Well-designed generic APIs** encode invariants once: “this map keys are **`String`** ids; values are **`User`** entities.”

Poor designs—**wildcard return types**, **raw** collections in DTOs, **`Optional<?>`** with no type info—create cast mazes and test gaps. This chapter consolidates patterns you reuse from persistence through HTTP handlers.

## 4. Conceptual Explanation

### 4.1 Generic class as service template

```java
class Repository<T> {
    private final Map<String, T> store = new HashMap<>();

    void save(String id, T value) { store.put(id, value); }
    Optional<T> find(String id) {
        return Optional.ofNullable(store.get(id));
    }
    int count() { return store.size(); }
}
```

- **`T`** is fixed per **`Repository<User>`** instance—callers see **`Optional<User>`**.
- **Erasure** (Ch.03) makes runtime store a **`Map`**, but compile-time **`get`** returns **`User`**.

### 4.2 Optional and generic fluency

```java
users.find("1").map(User::name).orElse("missing");
```

**`map`** preserves type transformations without null checks. Avoid **`Optional` fields** in DTOs; use for **return types** only.

### 4.3 When to use generic methods instead

Use **static utility methods** with their own `<T>` when the class is not parameterized but one operation is:

```java
public static <T> List<T> dedupe(List<T> input) { /* ... */ }
```

Use a **generic class** when **state** is consistently tied to one `T` (repository, connection pool of `T` resource wrapper).

### 4.4 PECS on public boundaries

| Method role | Signature hint |
|-------------|----------------|
| Read-only query over numbers | `Iterable<? extends Number>` |
| Sink accepting strings | `Collection<? super String>` |
| Fully read/write same type | `List<T>` with class/method `<T>` |

Do not slap `? extends Object` on everything—it defeats useful **`add`** on the caller side.

### 4.5 Defensive copies and generics

Returning **`List<T>`** from a getter while exposing internal **`ArrayList<T>`** lets callers mutate your state. Return **`List.copyOf(backing)`** or **unmodifiable** views—type argument preserved.

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Type-safe API** | Compiler enforces type arguments end-to-end |
| **Type witness** | Explicit `<User>` on a call when inference fails |
| **Bounded wildcard return** | Avoid—`List<? extends T>` returned limits caller `add` |
| **Type token** | `Class<T>` or reflective `Type` carrying erased argument |
| **Facade** | Simplified API hiding generic-heavy internals |

## 6. Syntax and Language Rules

```java
// Good — concrete, stable API
Repository<User> users = new Repository<>();

// Avoid — public method returning raw Map
// Map getAllUsers();

// Prefer
Map<String, User> snapshot() {
    return Map.copyOf(store);
}

// Wildcard in return position — rare, document why
List<? extends Payment> recentPayments() { }
```

**Record generic types** — `record Pair<A,B>(A first, B second)` generate accessors with type parameters preserved.

## 7. Step-by-Step Examples

### 7.1 Minimal — `GenericsApiDemo`

Path: [examples/core-java/generics-api-demo/](../examples/core-java/generics-api-demo/)

**Verified output:**

```text
found=Ada
missing=false
count=1
```

- **`save`** ties id and **`User`** entity.
- **`find("1").map(User::name)`** — **`Optional`** chain without casting.
- **`missing=false`** — `Optional.isPresent()` for absent key `"9"`.

### 7.2 Realistic — layered DTO

```java
public record Page<T>(List<T> items, int total) {
    public Page {
        items = List.copyOf(items);  // compact canonical constructor
    }
}
```

Callers receive **`Page<Order>`** with immutable **`List<Order>`**.

### 7.3 Advanced — passing type token for erasure gap

```java
static <T> T parse(String json, Class<T> type) {
  // framework uses Class<T> for runtime reification of T
}
```

Use library **`Type`** when `T` is **`List<User>`**, not merely **`User`**.

## 8. Real-World Use Cases

- **Spring Data `JpaRepository<T, ID>`** — CRUD surface parameterized by entity.
- **`java.util` collections** — factories `List.of`, maps `ConcurrentHashMap<K,V>`.
- **`Stream<T>`** — entire pipeline typed to element type.
- **Reactive APIs** — `Flux<T>` / `Mono<T>` (frameworks) carry element type through operators.

## 9. Internal Behavior

Callers compile against **generic signatures**; shipped **`.class`** files carry **Signature** attributes for reflection (`Signature` is not the same as reification—arguments appear in metadata but bytecode bodies remain erased).

## 10. Edge Cases and Limitations

- **Wildcard capture** in public APIs—prefer **named type parameters** when callers must supply values back into your structures.
- **Self-bounded generics** (`Enum<E extends Enum<E>>`) in framework enums—copy pattern, do not invent ad hoc.
- **`Optional<?>`** — rarely useful; loses compile-time map operations.
- **Binary compatibility** — changing type parameters on public classes can break source (not always binary) clients—semver carefully.

## 11. Common Mistakes

### Incorrect — raw repository map in public getter

```java
public Map getStore() {
    return store;
}
```

### Corrected

```java
public Map<String, T> findAll() {
    return Map.copyOf(store);
}
```

### Incorrect — returning `List<?>` without documenting read-only contract

Callers cannot pass result to another method expecting `List<User>` without friction.

### Corrected

Return `List<T>` copy or immutable interface with clear Javadoc.

## 12. Best Practices

- Parameterize **both** keys and values on maps in domain code: **`Map<String, OrderLine>`**.
- Use **`Optional<T>`** for “maybe absent” lookups; not for collections, not for fields.
- Document **null** policy on type arguments (many teams forbid null values in `List<T>`).
- Match Module 12 guidance: **interface type + generics** on signatures, concrete class at **`new`**.
- Re-read **PECS** when a method accepts **two** collections (source vs destination).

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| `Repository<T>` | One pattern, many entities | Reflection-heavy frameworks need extra metadata |
| Dedicated class per entity | Maximum explicitness | Duplication |
| Wildcard parameters | Flexible call sites | Harder for callers to reuse returned values |
| `Class<T>` parameter | Runtime type for `T` | Callers must supply token; easy to get wrong |

## 14. Performance Considerations

Generics do not dictate algorithmic cost; **`HashMap`** lookups remain O(1) average. **`Optional`** adds object allocation when present—acceptable for lookups; avoid in tight numeric loops.

## 15. Security Considerations

Typed APIs do not sanitize data. **`Repository<Object>`** accepting any deserialized type is dangerous—bind **`T`** to known entity types and validate inbound maps at trust boundaries.

## 16. Java Version and Compatibility Notes

**`Optional`** since Java 8. **`record`** generic declarations since Java 16. **`Map.copyOf`** since Java 10. **Java 21** path unchanged for patterns here.

## 17. Related Language Comparisons

Go uses **generics** (1.18+) with constraints; Java’s **wildcards** remain more prominent at API edges. Rust **monomorphization** compiles per `T`; Java shares one bytecode.

## 18. Revision and Mastery

- **Gaps:** using **`Optional`** as fields; exposing **`ArrayList`** through getters.
- **You should answer:** Why `missing=false` for id `"9"` in `GenericsApiDemo`?
- **Advanced:** design **`Cache<K,V>`** with `get`/`put` and PECS on bulk import methods.

## 19. Exercises

### Beginner

1. Predict `found=` and `count=` lines in `GenericsApiDemo`.
2. Add `boolean delete(String id)` returning whether an entry existed.

### Intermediate

3. Write `Page<User>` factory from `List<User>` and total count with defensive copy.
4. Explain when a DAO should be `interface UserDao` vs `Repository<User>`.

### Advanced

5. Add `void importAll(Collection<? extends T> incoming)` to `Repository`—why is `extends` correct?

## 20. Interview Questions

1. How do you expose a typed in-memory store without raw types?
2. Why not return `null` instead of `Optional<T>`?
3. PECS example on a method that copies into an existing list parameter.

## 21. Solutions

1. `Ada`; `1`.
2. `return store.remove(id) != null;`
3. Compact constructor with `List.copyOf`; record accessor returns typed list.
4. Interface documents contract; generic repo is reusable teaching pattern—real apps may split per aggregate.
5. Method only **reads** from `incoming` — producer **`extends T`**.

## 22. Chapter Summary

**Generic API design** carries type arguments through **classes**, **methods**, **maps**, and **optionals** so callers never guess casts. **`Repository<T>`** with **`Optional<T>`** lookups models persistence-style services; **`GenericsApiDemo`** verifies naming and presence checks. Combine **Module 12** collection types, **PECS** at boundaries, and **erasure-aware** patterns (`Class<T>`, copies, no raw exports) for maintainable libraries.

## 23. Key Terminology

Repository pattern · `Optional<T>` · defensive copy · type token · PECS · type-safe API

## 24. References and Further Reading

- Effective Java, 3rd ed. — Items 29–33 (generics and collections APIs)
- [Java SE `Optional`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Optional.html)
- [Module 12 Ch.04 — `Map<K,V>`](../12-collections-framework/04-maps-and-hashing.md)
- [Ch.02 — bounds and wildcards](02-bounds-and-wildcards.md)

## 25. Next Steps

- [Module 14 — Exceptions and Error Handling](../14-exceptions-and-error-handling/README.md) — typed failures at API boundaries
- Concurrency modules — generic concurrent collections later

## Review Checklist

- [x] API patterns, Optional, Map, PECS integration
- [x] `GenericsApiDemo` verified; M12 `HashMap` tie-in
- [x] Depth gate, exercises, references
