# Core Java Interviews

> **Status:** Completed  
> **Module:** 52 — Interview Preparation  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Answer **core Java** questions on types, OOP, collections, generics, and exceptions with precise language.
- Predict **output** and explain **equals/hashCode**, **String** behavior, and **autoboxing** pitfalls.
- Map question topics to **handbook modules** for targeted revision.
- Practice **structured answers**: definition → rule → example → trade-off.

## 2. Prerequisites

- **Modules 02–18** — fundamentals through Optional/null.
- **Module 12–13** — collections and generics (frequent interview focus).

## 3. Why This Concept Exists

Interview loops filter for **accurate mental models**, not trivia memorization. Candidates stumble on `==` vs `equals`, mutable keys in `HashMap`, generics erasure, and exception hierarchy—not because they lack years of experience, but because those details were never consolidated.

## 4. High-Yield Topics

### Types and memory

| Question theme | Key points | Handbook |
|----------------|------------|----------|
| `==` vs `equals` | References vs logical equality; override contract | M02, M06 |
| `String` immutability | pool, concatenation, `StringBuilder` | M11 |
| Autoboxing / NPE | `Integer` cache -128..127; unboxing null | M02 |

### OOP

| Theme | Key points |
|-------|------------|
| Inheritance vs composition | fragile base class | M07 |
| Interfaces vs abstract classes | multiple inheritance of type | M08 |
| Overload vs override | compile-time vs virtual dispatch | M04, M07 |

### Collections

| Theme | Key points |
|-------|------------|
| `List` vs `Set` vs `Map` | ordering, uniqueness | M12 |
| `HashMap` bucket/collision | `hashCode`+`equals`; immutable keys | M12 |
| `ConcurrentHashMap` | not same as synchronized map | M12, M24 |

### Generics

- **Erasure** — no `List<String>` at runtime; `ClassCastException` at bridge boundaries.
- **PECS** — producer `extends`, consumer `super`.
- Cannot `new T()`.

### Exceptions

- Checked vs unchecked philosophy.
- try-with-resources and suppressed exceptions (M14).

## 5. Sample Questions with Answer Shape

**Q: Why must equal objects have equal hash codes?**  
**A:** `HashMap` uses hash to choose bucket; unequal hashes break `Map` contract → lookups fail for equal keys. Override both together (M06).

**Q: What does `final` mean on a reference field?**  
**A:** Reference cannot be reassigned; object state may still mutate unless immutable (M06).

**Q: Difference `ArrayList` vs `LinkedList`?**  
**A:** Array-backed random access O(1) vs node list; middle insert differs; memory locality favors `ArrayList` usually (M12).

## 6. Code Reading Drills

Practice predicting output then verify in `jshell` or small `main` (Temurin 21). Example pattern:

```java
Map<String, Integer> m = new HashMap<>();
String s = new String("a");
m.put(s, 1);
s = new String("a");
System.out.println(m.get(s));
```

Explain: keys equal if `equals` true; separate `new String` instances with same content still match if `equals` overridden correctly → prints `1`.

## 7. Common Candidate Mistakes

- Saying “generics are only syntax sugar” without mentioning erasure consequences.
- Claiming `volatile` makes compound operations atomic.
- Confusing `ConcurrentModificationException` cause (fail-fast iterator).

## 8. Revision and Mastery

- Explain `equals` contract five clauses informally.
- Whiteboard `HashMap.put` flow at high level.

## 9. Exercises

1. Write five self-quiz questions from Modules 11–13 with answers hidden.
2. Record 2-minute spoken answer on interfaces vs abstract classes.

## 10. Chapter Summary

Core Java interviews reward precise contracts: equality, collections, generics erasure, OOP dispatch, and exceptions. Use handbook modules as revision map; always add a small example.

## Next Steps

[02-jvm-and-concurrency-interviews.md](02-jvm-and-concurrency-interviews.md)

## Review Checklist

- [x] Topic map; sample Q&A; drills
