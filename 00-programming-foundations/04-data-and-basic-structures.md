# Data and Basic Structures

> **Status:** Completed  
> **Module:** 00 — Programming Foundations  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Oracle JDK 21.0.9 (`javac 21.0.9`)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain what **data** and **state** mean in a running program.
- Distinguish values, variables, and types at a conceptual level.
- Use Java arrays for ordered collections of the same type.
- Describe basic structure ideas: list-like sequences, key/value pairing (conceptually), and why structure choice matters.
- Build a tiny “scoreboard” with parallel arrays and explain its limitations.
- Preview why objects and collections (later modules) replace parallel arrays in real code.

## 2. Prerequisites

- Chapters [01](01-what-is-programming-and-what-is-java.md), [02](02-how-computers-execute-programs.md), [03](03-algorithms-and-problem-solving.md)

## 3. Why This Concept Exists

Programs are useless without data. Before object-oriented design, you need:

- What it means to store information
- How order and indexing work
- Why “a bunch of related values” needs a clear structure

This chapter is the bridge from algorithms-on-paper to algorithms-on-real-memory-shaped values in Java.

## 4. Conceptual Explanation

### 4.1 Data and state

**Data** is information your program works with: numbers, text, true/false flags, collections of those.

**State** is the data a program (or part of a program) holds **right now**. As statements run, state changes:

```text
score = 0          // state: score is 0
score = score + 1  // state: score is 1
```

Objects (Module 05+) bundle state (fields) with behavior (methods). Here we start with simple variables and arrays.

### 4.2 Values, variables, types

| Idea | Meaning | Example |
|------|---------|---------|
| **Value** | A concrete piece of data | `42`, `"Ada"` |
| **Variable** | A named place that holds a value | `int score = 42;` |
| **Type** | What kind of values are allowed | `int`, `String`, `int[]` |

Java is **statically typed**: the compiler checks types before runtime (Module 02 and 10 deepen this). For now: if you say `int`, you store integers, not arbitrary text.

### 4.3 Primitive vs reference (first contact)

Java has:

- **Primitive types** such as `int`, `boolean`, `double` — hold the value itself.
- **Reference types** such as arrays and (later) objects — a variable holds a *reference* to data elsewhere in memory.

```text
int x = 5;           // x holds 5
int[] nums = {1,2};  // nums refers to an array object
```

**Assignment semantics (critical for arrays).**

```java
int a = 5;
int b = a;   // b gets a copy of the value; changing b does not change a

int[] one = {1, 2, 3};
int[] two = one;  // two gets a copy of the REFERENCE, not a new array
two[0] = 99;
// one[0] is now 99 as well — both variables point at the same array object on the heap
```

| Kind | What the variable holds | Assignment copies |
|------|-------------------------|-------------------|
| **Primitive** (`int`, `boolean`, …) | The value itself | The value |
| **Reference** (`int[]`, `String`, objects) | A reference (handle) to heap data | The reference only—**not** a deep copy of the array/object |

Module **25** maps this to stack versus heap drawings. Beginners should remember: **`int[] b = a;` does not duplicate elements** unless you explicitly copy (for example `Arrays.copyOf` in later modules).

### 4.4 Arrays: ordered sequence

An **array** stores a fixed number of elements of one type, indexed from `0`.

```java
int[] scores = {95, 88, 92};
// indexes:       0   1   2
```

| Operation | Idea |
|-----------|------|
| Read | `scores[1]` → `88` |
| Write | `scores[1] = 90;` |
| Length | `scores.length` → `3` |

Arrays do not resize. Growing requires creating a new larger array and copying—Java’s `ArrayList` (Module 12) wraps that pattern.

### 4.5 Other basic structures (conceptual)

| Structure | Idea | Java preview |
|-----------|------|--------------|
| List / sequence | Ordered items | array, later `List` |
| Set | Unique items | later `Set` |
| Map | Key → value | later `Map` |
| Record of related fields | Name + score together | later class/record |

**Parallel arrays** (names[i] with scores[i]) work for tiny demos but break easily—easy to update one array and forget the other.

**Why classes exist (preview).** A **class** lets you declare a new type that **groups fields that belong together**:

```text
class Player {
  String name;
  int score;
}
```

Then one `Player[]` or list holds both name and score per row—no fragile “index 2 means the same person in two arrays” convention. Classes also attach **methods** to that data (`player.addPoints(5)`). Module **05** defines classes, constructors, and objects; Module **12** shows resizable `List<Player>`. Parallel arrays are a teaching scaffold, not how production domain models stay consistent.

## 5. Terminology

| Term | Meaning |
|------|---------|
| Data | Information manipulated by a program |
| State | Current values held by the program/component |
| Variable | Named storage for a value |
| Type | Constraint on allowed values |
| Array | Fixed-length indexed sequence of one element type |
| Index | Integer position starting at 0 |
| Reference | Handle pointing to an array/object in memory |

## 6. Syntax and Language Rules

```java
int[] scores = new int[3];     // length 3, values default to 0
int[] scores2 = {95, 88, 92};  // create and initialize
String[] names = {"Ada", "Grace"};
```

Rules to remember:

- Indexes from `0` to `length - 1`
- Access outside that range → `ArrayIndexOutOfBoundsException` at runtime
- `length` is a field for arrays, not a method (`scores.length`, not `scores.length()`)
- Enhanced for-loop: `for (int score : scores) { ... }` reads each element

## 7. Step-by-Step Examples

### 7.1 Minimal — index mental model

```text
names:  [ "Ada", "Grace", "Alan" ]
index:     0       1        2
scores: [  95,    88,      92  ]
```

`names[2]` and `scores[2]` both refer to Alan’s row **by convention**—the language does not enforce the pairing.

### 7.2 Realistic — scoreboard

Runnable: [examples/beginner/scoreboard/](../examples/beginner/scoreboard/)

```java
public class Scoreboard {
    public static void main(String[] args) {
        String[] names = {"Ada", "Grace", "Alan"};
        int[] scores = {95, 88, 92};

        System.out.println("Scoreboard:");
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i] + " -> " + scores[i]);
        }

        int total = 0;
        for (int score : scores) {
            total += score;
        }
        double average = (double) total / scores.length;
        System.out.println("Average: " + average);
    }
}
```

**Verified output:**

```text
Scoreboard:
Ada -> 95
Grace -> 88
Alan -> 92
Average: 91.66666666666667
```

**Why `(double) total` appears.** In Java, `int / int` performs **integer division**: fractional parts are discarded (`7 / 2` is `3`, not `3.5`). Here, `total` and `scores.length` are both `int`, so `total / scores.length` would truncate the average.

`(double) total` is a **cast**: it tells the compiler to treat `total` as a `double` for this expression. The division then uses floating-point rules, so you get a decimal average. Casts can lose precision in other contexts; here the goal is simply to avoid silent truncation. Module **02** covers numeric types, promotion, and casting rules systematically.

### 7.3 Toward better structure (preview)

Better long-term model (not required to compile yet):

```text
Player { name: "Ada", score: 95 }
Player { name: "Grace", score: 88 }
```

That is why classes exist (Module 05–06). Parallel arrays are a teaching scaffold.

## 8. Real-World Use Cases

- Tables of measurements (time series samples)
- Image pixels as 2D arrays (later)
- Configuration lists
- Batch IDs

Production Java usually prefers collections and domain objects over raw parallel arrays.

## 9. Internal Behavior

Arrays are objects on the heap (JVMS/Java SE model). A variable of array type stores a reference. Passing an array to a method passes the reference—callee can modify element contents. Details: Modules 04 and 25.

## 10. Edge Cases and Limitations

- Fixed length
- Single element type (unless you use `Object[]`, which loses type safety—avoid for now)
- `null` array reference vs empty array length 0 — different ideas
- Multidimensional arrays exist (`int[][]`) but add complexity—Module 11

## 11. Common Mistakes

### Incorrect

```java
for (int i = 0; i <= names.length; i++) { ... } // uses <=
```

### Corrected

Use `i < names.length`. `<=` walks one past the end.

### Incorrect

Assuming `names` and `scores` stay aligned after ad-hoc inserts.

### Corrected

Use one structure that keeps related fields together (objects/lists of objects).

### Incorrect

`scores.length()` 

### Corrected

`scores.length` for arrays; `list.size()` for `List` later.

## 12. Best Practices

- Name arrays after what they hold (`scores`, not `arr`).
- Keep length checks consistent.
- Comment when parallel arrays are temporary teaching tools.
- Prefer enhanced for-loop when you do not need the index.

## 13. Design Trade-offs

| Structure | Pros | Cons |
|-----------|------|------|
| Array | Simple, fast index access, low ceremony | Fixed size, weak modeling of entities |
| Parallel arrays | Easy demo | Fragile alignment |
| List of objects | Cohesive, scalable design | Needs OOP knowledge |

## 14. Performance Considerations

Array index access is typically very fast. Prematurely avoiding arrays “for performance” is misguided—choose structures for clarity first. Module 12 discusses complexity of collections.

## 15. Security Considerations

Never use unvalidated user input as an array index. Huge requested sizes can exhaust memory (Module 43).

## 16. Java Version and Compatibility Notes

Arrays are foundational since early Java. Example validated on **21.0.9**.

## 17. Related Language Comparisons

Many languages have arrays/lists/maps with different mutability defaults. Java’s arrays are fixed-length; resizable lists are separate (`ArrayList`). Learn Java’s distinctions rather than assuming another language’s rules.

## 18. Revision and Mastery

### Should already know

Reference semantics for arrays; `Clone` pitfalls; why `Arrays.equals` vs `==`.

### Gaps to check

- Mutating arrays inside methods as accidental API surface
- Using `Object[]` to dodge generics (Module 13)
- Confusing array covariance quirks (advanced type system)

### Questions

1. What does `int[] a = b;` copy?
2. Why are parallel arrays considered a design smell?
3. Empty array vs `null`—when does each appear?

## 19. Exercises

### Beginner

1. Draw memory sketch of `names` and `scores` from Scoreboard.
2. Print only players with score >= 90.

### Intermediate

3. Find the name of the player with the highest score (combine Ch.03 algorithm).
4. Explain what happens if `scores` has a different length than `names`—then guard against it.

### Advanced

5. Redesign scoreboard as a single array of a small class (after peeking Module 05) or document why you cannot yet.
6. Show a method that swaps two scores by index; discuss whether names must swap too.

## 20. Interview Questions

1. **Array vs list?** — Array fixed length; list typically resizable abstraction.  
2. **Why 0-based indexing?** — Historical/address arithmetic convenience; Java follows that convention.  
3. **What is program state?** — Current data values held during execution.  
4. **Reference vs primitive?** — Primitive holds value; reference points to object/array.  

## 21. Solutions

1. Two separate arrays; indexes align by convention.  
2. Loop with `if (scores[i] >= 90) print names[i]`.  
3. Track best index while scanning scores; print `names[bestIndex]`.  
4. Check `names.length == scores.length` at start; throw or abort if not.  
5. Class `Player { String name; int score; }` + `Player[]` or `List<Player>`.  
6. Swap both arrays’ elements at those indexes—or swap one object containing both fields.

## 22. Chapter Summary

Data is information; state is data-now. Variables hold typed values; arrays hold fixed-length sequences with 0-based indexes. Parallel arrays teach indexing but objects/collections are the professional path.

## 23. Key Terminology

Data · state · variable · type · primitive · reference · array · index · parallel arrays

## 24. References and Further Reading

- [JDK 21 API — java.lang](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/package-summary.html) (arrays as objects conceptually via reflection/docs)
- Next: [05-complexity-and-debugging-mindset.md](05-complexity-and-debugging-mindset.md)
- Later: Module 02 types; Module 11 arrays/strings; Module 12 collections

## 25. Next Steps

[05-complexity-and-debugging-mindset.md](05-complexity-and-debugging-mindset.md) — thinking about cost and finding bugs systematically.

## Review Checklist

- [x] Learning objectives covered
- [x] Scoreboard verified on JDK 21.0.9
- [x] Depth gate passed
- [x] Revision and Mastery included
