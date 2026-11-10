# Arrays

> **Status:** Completed  
> **Module:** 11 — Arrays and Strings  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Declare, create, and initialize **arrays** of primitives and references.
- Use **`length`**, indexing, and bounds behavior (`ArrayIndexOutOfBoundsException`).
- Iterate with **`for`**, enhanced **`for`**, and `while` when order matters.
- Apply **`java.util.Arrays`** helpers (`copyOf`, `sort`, `toString`, `binarySearch`).
- Model **2D** and higher-dimensional arrays as arrays of arrays.
- Explain how arrays relate to **reference semantics** (Module 03 Ch.01).

## 2. Prerequisites

- **Module 02** — primitives, references, operators.
- **Module 03** — loops and control flow.
- **Module 06** — objects; arrays of reference types hold **references**, not embedded objects.

Brief refresher: an **array** is a fixed-length, ordered sequence of slots. Each slot has the **same element type**. For reference element types, each slot holds a reference (or `null`).

## 3. Why This Concept Exists

Many algorithms process **homogeneous sequences**: test scores, pixel rows, board squares, batch ids. Variable names (`score1`, `score2`, …) do not scale. Arrays give **indexed bulk storage** with **O(1)** random access by index.

Java chose **fixed length** at creation (unlike resizable `ArrayList` in Module 12) so layout is simple for the JVM and bytecode: one object header, a length field, and a contiguous run of elements.

## 4. Conceptual Explanation

### 4.1 Array objects on the heap

An array is a **reference type** object. `int[] scores` declares a reference variable; `scores = new int[3]` allocates an array object whose **length** is 3 and whose elements are default-initialized (`0` for `int`, `null` for references).

```java
int[] scores = new int[3];  // [0, 0, 0]
scores[0] = 95;
```

### 4.2 Length is fixed

`scores.length` is a **public final field** (not a method). You cannot grow an array in place; you allocate a new array and copy if needed (`Arrays.copyOf`).

### 4.3 Indexing

Valid indices are **`0` through `length - 1`**. Index `-1` or `length` throws **`ArrayIndexOutOfBoundsException`** at runtime—the compiler does not prove index safety.

### 4.4 Reference arrays and aliasing

```java
int[] a = {1, 2, 3};
int[] b = a;   // same array object
b[0] = 99;     // visible through a[0] too
```

Assignment copies the **reference**, not a deep copy of elements.

### 4.5 2D arrays

`char[][] grid` is an array of **`char[]` rows**. Rows can differ in length (**ragged** arrays), though often they are rectangular.

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Element type** | Type of every slot (`int`, `String`, …) |
| **Length** | Fixed count of elements; `array.length` |
| **Index** | Zero-based position into the array |
| **Default initialization** | Numeric `0`, `boolean` false, references `null` |
| **Component type** | Element type in JVM array descriptors |
| **Ragged array** | 2D array whose rows have different lengths |

## 6. Syntax and Language Rules

```java
int[] a;              // declaration — variable name is `a`
int b[];              // legal but discouraged style

int[] nums = new int[5];
int[] preset = { 10, 20, 30 };           // array initializer
int[] copy = preset;                     // alias

String[] names = new String[2];          // [null, null]
names[0] = "Ada";
```

- **Array initializer** `{ ... }` only allowed in declarations or `new Type[] { ... }`.
- **`new int[] {1,2}`** required when passing inline to a method: `method(new int[] {1, 2});`.

## 7. Step-by-Step Examples

### 7.1 Minimal — fill and read

```java
double[] temps = new double[3];
temps[0] = 20.5;
System.out.println(temps.length);  // 3
```

### 7.2 Realistic — ArraysDemo

Full source: [`ArraysDemo.java`](../../examples/core-java/arrays-demo/ArraysDemo.java).

```bash
javac ArraysDemo.java && java ArraysDemo
```

**Verified output (Temurin 21.0.12.1):**

```text
len=3
max=95
sorted=[85, 90, 95]
grid01=b
```

**Important lines:**

- Linear scan finds **max** without sorting—`O(n)`.
- **`Arrays.copyOf`** avoids mutating the original when sorting.
- **`Arrays.sort`** sorts primitives in ascending order (dual-pivot quicksort in OpenJDK for `int[]`).
- **`grid[0][1]`** — row 0, column 1 → `'b'`.

### 7.3 Advanced — binary search after sort

```java
int[] ids = { 3, 9, 15 };
int idx = Arrays.binarySearch(ids, 9);  // 1 if sorted ascending
```

`binarySearch` requires a **sorted** array; otherwise results are undefined.

## 8. Real-World Use Cases

- **Batch processing** — fixed-size buffers for I/O and SIMD-friendly layouts.
- **Matrices and grids** — game boards, image rows (`int[][]` or flat `int[]` with stride).
- **Interop** — JNI and many libraries expose C-style arrays.
- **Varargs** — `void f(String... args)` allocates `String[]` at call site.

## 9. Internal Behavior

JVM array objects store **length** and elements in a structure similar to objects with an extra data section (JVMS). `aaload`/`iastore` bytecode instructions index with runtime bounds checks.

**HotSpot** can eliminate bounds checks in tight loops when static analysis proves index safety (dependent on version and loop shape).

Arrays implement **`Cloneable`** and **`Serializable`**; `clone()` is shallow—new array object, same element references for reference types.

## 10. Edge Cases and Limitations

- **Maximum length** — bounded by `Integer.MAX_VALUE - 8` (implementation limit); huge arrays risk `OutOfMemoryError`.
- **`length` zero** — legal: `new int[0]`.
- **`null` array reference** — `null.length` → `NullPointerException`.
- **Covariant arrays** — `String[]` is a subtype of `Object[]` but storing a non-`String` throws `ArrayStoreException` (differs from generics—Module 10 Ch.03).

## 11. Common Mistakes

### Incorrect — off-by-one loop bound

```java
for (int i = 0; i <= arr.length; i++) { /* ... */ }
```

### Corrected

```java
for (int i = 0; i < arr.length; i++) { /* ... */ }
```

### Incorrect — assuming assignment copies elements

```java
int[] original = {1, 2};
int[] shadow = original;
shadow[0] = 0;  // original[0] is now 0
```

### Corrected — explicit copy when independence is required

```java
int[] independent = Arrays.copyOf(original, original.length);
```

## 12. Best Practices

- Prefer **`for (int x : arr)`** when you need every element and not the index.
- Use **`Arrays.toString`** for debug output instead of manual loops.
- For resizable sequences, use **`ArrayList`** (Module 12); keep arrays for fixed size and performance-critical primitives.
- Name lengths clearly: `rowCount`, `columnCount`; avoid magic indices—use named constants.

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Fixed-size array | Simple, fast index access | Resizing requires new allocation |
| `int[]` vs `Integer[]` | No boxing; compact | Cannot use with generics collections directly |
| 2D `[][]` vs flat `[]` | Readable `[row][col]` | One extra indirection per row; ragged vs rectangular choice |
| Sort in place | No extra memory | Destroys original order |

## 14. Performance Considerations

Random access is **constant time**. Searching unsorted data is **linear**. Sorting is **O(n log n)**. For primitive arrays, contiguous storage helps CPU cache locality.

## 15. Security Considerations

Arrays do not auto-validate untrusted indices from network input. Validate lengths and indexes at trust boundaries before use.

## 16. Java Version and Compatibility Notes

Arrays exist since Java 1.0. **`java.util.Arrays`** gained stream helpers in Java 8+; prefer them when they match your read pattern.

## 17. Related Language Comparisons

C/C++ arrays are often stack-allocated or decay to pointers; Java arrays are **always heap objects** with bounds checks (unless optimized away).

## 18. Revision and Mastery

- **Gaps:** confusing `length` (field) with `String.length()` (method).
- **You should answer:** Why does `int[] a = {1}; int[] b = a; b[0]=2` change `a[0]`?
- **Advanced:** `System.arraycopy`, memory layout for large `byte[]` in NIO.

## 19. Exercises

### Beginner

1. Create `int[4]`, set all to `7`, print `Arrays.toString`.
2. Predict `ArraysDemo` `max=` without running.

### Intermediate

3. Write `indexOf(int[] a, int target)` returning first index or `-1`.
4. Transpose a rectangular `int[][]` into a new array.

### Advanced

5. Explain why `Object[] o = new String[1]; o[0] = new Object();` fails at runtime but compiles.

## 20. Interview Questions

1. Difference between array length and `ArrayList.size()`?
2. Is `int[]` passed by reference or value?
3. What is the default value of `new String[3]` elements?

## 21. Solutions

1. `[7, 7, 7, 7]`
2. `95`
3. Loop from `0` to `length-1`
4. Swap dimensions with new allocation
5. Array covariance: compile-time store check uses runtime element type

## 22. Chapter Summary

**Arrays** are fixed-length, indexed sequences on the heap. **`length`** is fixed at creation; indices are **0-based**. **`ArraysDemo`** shows scan, **copy + sort**, and **2D** access. Reference arrays **alias** when assigned. Next: **`String`** as the other dominant sequence type in Java.

## 23. Key Terminology

Array · length · index · default initialization · ragged array · `Arrays`

## 24. References and Further Reading

- [JLS §10 — Arrays](https://docs.oracle.com/javase/specs/jls/se21/html/jls-10.html)
- [Java SE `java.util.Arrays`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html)

## 25. Next Steps

- [02-strings-and-immutability.md](02-strings-and-immutability.md)
- Module 12 — `List` and resizable collections built on arrays internally

## Review Checklist

- [x] Learning objectives covered
- [x] Depth gate met; `ArraysDemo` verified Temurin 21.0.12.1
- [x] Revision and Mastery, exercises, references
- [x] Prerequisites refreshed in place
