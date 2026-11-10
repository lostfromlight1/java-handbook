# Method Basics

> **Status:** Completed  
> **Module:** 04 — Methods and Functions  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain what a method is and why programs group code into methods.
- Read and write a method declaration: modifiers, return type, name, parameters, body.
- Distinguish **parameters** (declaration) from **arguments** (call site).
- Choose `void` vs a value-returning type and use `return` correctly, including guard clauses.
- Call methods in a sensible order and compose small methods into larger behavior.
- Declare **local variables** inside methods and respect their scope.
- Follow Java naming conventions for methods.
- Recognize that demos use `static` helpers while instance methods require an object (full treatment in Ch.04).
- State at a high level that Java passes arguments **by value** (detailed in Ch.03).

## 2. Prerequisites

- **Module 01** — project layout, `javac` / `java`, the `main` entry point.
- **Module 02** — variables, types, `final`, local vs field (preview).
- **Module 03** — `if` / `else`, boolean conditions, guard-style early exits in conditionals.

Brief refresher: a **class** groups related code. The JVM starts execution at `public static void main(String[] args)` unless you configure another entry (advanced). Everything in this chapter lives inside one class, `MethodsBasicsDemo`, so you can focus on method shape without object construction.

## 3. Why This Concept Exists

Real programs are not one long sequence of statements in `main`. They are thousands of named **operations**—validate input, compute tax, send mail—that must be **reused**, **tested**, and **read** like a table of contents.

**Methods** (Java’s term; some languages say “functions”) solve three problems at once:

| Problem | How methods help |
|---------|------------------|
| Duplication | One definition, many call sites |
| Complexity | Hide detail behind a meaningful name |
| Verification | Exercise one unit of behavior in isolation |

Without methods, every feature would copy-paste logic; a one-line fix might require hunting through an entire file. With methods, you name intent (`clamp`, `describeScore`) and keep `main` as a thin orchestrator.

## 4. Conceptual Explanation

### 4.1 What a method is

A **method** is a named block of code attached to a class. It can accept inputs (**parameters**), perform work (**body**), and optionally produce an output (**return value**). Calling a method transfers control to that block; when the method finishes (normally or via `return`), control returns to the caller.

```java
static int sum(int a, int b) {
    return a + b;
}
```

Here `sum` is the method name, `a` and `b` are parameters, and the method **returns** an `int` to whoever called it.

### 4.2 Declaration anatomy

A typical method declaration has this shape:

```text
[modifiers] returnType methodName(parameterList) {
    // body
}
```

| Part | Role | Example in demo |
|------|------|-----------------|
| Modifiers | Visibility, `static`, `final`, etc. | `static` (no object needed for call) |
| Return type | Type of value sent back, or `void` | `int`, `String`, `void` |
| Name | Identifier callers use | `greet`, `clamp` |
| Parameter list | Comma-separated `(type name, ...)` | `(int a, int b)` |
| Body | Statements in `{ }` | assignments, `if`, `return` |

The Java Language Specification (JLS) treats method declarations as part of a class body (JLS §8.4). The compiler checks that every path in a non-`void` method either returns a compatible value or throws (see §11).

### 4.3 Parameters vs arguments

| Term | Where | Meaning |
|------|--------|---------|
| **Parameter** | Declaration | Formal name in the method header, e.g. `String name` in `greet` |
| **Argument** | Call site | Actual value passed in, e.g. `"Ada"` in `greet("Ada")` |

```java
greet("Ada");           // argument is the literal "Ada"
static void greet(String name) { ... }  // parameter is name
```

**Parameter scope:** from entry to the method until the method exits, the parameter names are **local variables** visible only inside that method’s body. They shadow same-named fields in the class (rare in beginners’ code; fields are covered in Module 05).

### 4.4 `return` vs `void`

- **`void`** — the method performs an action and returns **no** value to the caller. You may use a bare `return;` to exit early.
- **Value-returning** — the return type (`int`, `String`, …) promises the caller will receive a value of that type when the method completes normally.

```java
static void greet(String name) {
    System.out.println("Hello, " + name + "!");
    // implicit end = return from void method
}

static int sum(int a, int b) {
    return a + b;   // required: sends int back to caller
}
```

The `return` statement **immediately** ends the method. Any code after `return` in the same block is **unreachable** (compile error).

### 4.5 Guard clauses and early return

Module 03 introduced guards for readability. In methods, **guard clauses** are early `return` statements that handle edge cases so the main logic stays flat:

```java
static String describeScore(int score) {
    if (score < 50) {
        return "failing";
    }
    return "passing";
}
```

If `score` is below 50, the method returns `"failing"` and never evaluates the final line. Otherwise it returns `"passing"`. This is the same idea as nested `if` chains, but with less indentation.

### 4.6 Calling methods; call order

To **invoke** (call) a method, use its name with arguments in parentheses:

```java
sum(3, 5);
greet("Ada");
```

**Call order** matters when one method depends on another’s result:

```java
System.out.println("sum(3, 5) = " + sum(3, 5));
```

First `sum(3, 5)` runs and produces `8`; then string concatenation builds the line. Methods can call methods:

```java
static int maxOfThree(int a, int b, int c) {
    return max(max(a, b), c);   // inner max runs before outer
}
```

The JVM executes the callee to completion before resuming the caller (unless you introduce threads later). Chapter 03 of this module (stack frames) will show how nested calls stack up; here, think “inner calls finish first.”

### 4.7 Local variables inside methods

A **local variable** is declared inside a method (or block). It exists only while that method invocation runs.

```java
static int clamp(int value, int min, int max) {
    // value, min, max are parameters (also locals)
    if (value < min) {
        return min;
    }
    ...
}
```

Locals must be **definitely assigned** before use (compiler error otherwise). Parameters count as assigned on entry.

### 4.8 Naming conventions for methods

Java style (and `javax` naming guidelines used across the JDK):

- **Methods:** `lowerCamelCase`, verb or verb phrase — `calculateTotal`, `isEmpty`, `clamp`.
- **Boolean queries:** often `is…`, `has…`, `can…` (`isValid()`).
- Avoid abbreviations unless universal (`max`, `min` ok in tiny helpers; prefer `findMaximum` in public APIs).

Names should describe **effect** or **result**, not implementation (`hashCode`, not `callXxHash`).

### 4.9 Preview: instance vs `static` methods

This chapter’s examples use **`static` methods**: you call them as `MethodsBasicsDemo.sum(...)` or, from inside the same class, simply `sum(...)`.

An **instance method** belongs to each object of the class and typically reads or updates that object’s state. You need a reference, e.g. `account.deposit(100)`. **Chapter 04** (`04-static-instance-and-method-references.md`) covers `static` vs instance, `this`, and method references in depth. For now: demos use `static` so `main` can call helpers without constructing objects.

### 4.10 Preview: pass-by-value

When you call `sum(3, 5)`, Java copies the **values** of `3` and `5` into the parameters `a` and `b`. The method cannot change the caller’s local variables through those parameters (for primitives, the copy is independent). That is **pass-by-value**. Reference types pass a copy of the reference; the detailed rules and diagrams are in [03-pass-by-value-and-stack-frames.md](03-pass-by-value-and-stack-frames.md)—one sentence to remember: **arguments are always value-copied into parameters; Java does not pass variables by reference.**

## 5. Terminology

| Term | Meaning |
|------|---------|
| Method | Named code block in a class |
| Declaration | Header plus body that defines the method |
| Signature | Name plus parameter types (overloading uses this; return type alone does not) |
| Parameter | Variable in the declaration |
| Argument | Expression passed at the call site |
| Return type | Type of value returned, or `void` |
| Invoke / call | Transfer control to a method |
| Local variable | Variable declared in a method or block |
| Guard clause | Early `return` (or throw) that handles a failure case |

## 6. Syntax and Language Rules

```java
modifiers? returnType identifier ( FormalParameterList ) MethodBody
```

**Rules you use daily:**

- Every non-`void` method must return a value on all paths that can complete normally (compiler enforces).
- `return` with no expression is allowed only in `void` methods.
- Parameters cannot be redeclared in the same list; names must be unique in the method scope.
- Overloading (same name, different parameter lists) is Chapter 02—not “same name, different return type only.”

**`main` signature** — the standard entry point the launcher expects:

```java
public static void main(String[] args)
```

Changing parameter type to `String` or omitting `static`/`public` breaks the default launch convention (advanced launchers can specify another method; beginners should keep the canonical form).

## 7. Step-by-Step Examples

Full program: [examples/core-java/methods-basics-demo/](../examples/core-java/methods-basics-demo/)

```bash
javac MethodsBasicsDemo.java
java MethodsBasicsDemo
```

### 7.1 Minimal: `void` greeting and `int` sum

```java
static void greet(String name) {
    System.out.println("Hello, " + name + "!");
}

static int sum(int a, int b) {
    return a + b;
}
```

- `greet` — side effect only (prints); no value to assign.
- `sum` — pure computation; caller can use `int total = sum(1, 2);`.

### 7.2 Realistic: guards and bounded value

`describeScore` and `clamp` show production-style validation and normalization:

```java
static String describeScore(int score) {
    if (score < 50) {
        return "failing";
    }
    return "passing";
}

static int clamp(int value, int min, int max) {
    if (value < min) {
        return min;
    }
    if (value > max) {
        return max;
    }
    return value;
}
```

`clamp` assumes `min <= max`; if not, behavior still returns a bound but may surprise callers—document or assert in real APIs.

### 7.3 Advanced: composing calls from `main`

```java
public static void main(String[] args) {
    greet("Ada");
    System.out.println("sum(3, 5) = " + sum(3, 5));
    System.out.println("describeScore(85) = " + describeScore(85));
    System.out.println("describeScore(40) = " + describeScore(40));
    System.out.println("clamp(150, 0, 100) = " + clamp(150, 0, 100));
    System.out.println("clamp(-5, 0, 100) = " + clamp(-5, 0, 100));
}
```

`main` does not implement business rules; it **demonstrates** helpers. That separation is how services grow: orchestration at the top, reusable pieces below.

**Verified output (Temurin JDK 21.0.12.1):**

```text
Hello, Ada!
sum(3, 5) = 8
describeScore(85) = passing
describeScore(40) = failing
clamp(150, 0, 100) = 100
clamp(-5, 0, 100) = 0
```

## 8. Real-World Use Cases

- **Validation methods** — `isValidEmail`, `normalizePhone` called from controllers before persistence.
- **Pricing / rules** — `calculateDiscount`, `clampQuantity` shared between web and batch jobs.
- **Mapping layers** — `toDto(Order)` keeps conversion out of `main` and testable.
- **Test doubles** — small pure functions (`sum`, `clamp`) are easy to unit-test without starting a server.

## 9. Internal Behavior

**Language level (JLS):** invoking a method causes argument expressions to be evaluated left-to-right; then control enters the method body (JLS §15.12). `return` completes the method and supplies a value to the invocation expression.

**JVM level (preview):** each active method call corresponds to a **stack frame** (local variables, operand stack, return address). Nested calls stack frames until they return. HotSpot implements this per JVMS §2.6; you do not manage frames manually. Chapter 03 in this module diagrams frames.

## 10. Edge Cases and Limitations

- **Missing return on a path** — `if` without `else` may leave a non-`void` method without `return` (compile error).
- **`void` method “returning” a value** — illegal: `return 5;` inside `void greet`.
- **Recursive calls without base case** — infinite recursion until `StackOverflowError` (Chapter 02).
- **Method too large** — readability and JIT inlining trade-offs; split when a method does more than one job.

## 11. Common Mistakes

### Incorrect — missing return on some path

```java
static int sign(int n) {
    if (n > 0) {
        return 1;
    }
    // compiler: missing return statement
}
```

### Corrected

```java
static int sign(int n) {
    if (n > 0) {
        return 1;
    }
    if (n < 0) {
        return -1;
    }
    return 0;
}
```

### Incorrect — wrong `main` signature

```java
public static void main(String args) { }  // not String[]
static void main(String[] args) { }        // not public
```

### Corrected

```java
public static void main(String[] args) { }
```

### Incorrect — value in `void` method

```java
static void logOk() {
    return true;  // error: incompatible types
}
```

### Corrected

```java
static void logOk() {
    System.out.println("ok");
    return;  // optional explicit exit
}
```

### Incorrect — using a non-`void` method as statement without caring about result

```java
sum(1, 2);  // legal but wasteful if you ignore the result—often a bug
```

### Corrected

```java
int total = sum(1, 2);
// or document deliberate fire-and-forget only for void methods
```

## 12. Best Practices

- Keep methods **focused**; one level of abstraction per method.
- Prefer **guard clauses** over deep nesting for validation.
- Use **meaningful names** over comments that restate the name.
- Make **pure** helpers (`sum`, `clamp`) `static` when they do not need instance state—easier to test.
- Document public methods with Javadoc (`/** ... */`) in real libraries; the demo uses short comments.

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Many small methods | Testable, readable names | More navigation |
| Large monolithic `main` | Fast to write in tutorials | Hard to maintain |
| `void` + side effects | Simple I/O demos | Harder to compose in pipelines |
| Value-returning pure methods | Easy to combine and test | Caller must use the result |

## 14. Performance Considerations

Method calls have small fixed cost; HotSpot **inlines** tiny hot methods (JIT detail, not a language guarantee). Prefer clarity first; micro-optimize only with profiling (Module 48+).

## 15. Security Considerations

Methods that **validate** input (`clamp` bounds, score rules) should run on the **server** for security-sensitive rules—never trust client-only checks (see later security modules).

## 16. Java Version and Compatibility Notes

Method declarations are core Java. Validated on **21.0.12.1** (Temurin). No preview features used in this chapter.

## 17. Related Language Comparisons

- **C:** functions are not inside types; Java requires methods live in a class (except hidden nest/local cases—advanced).
- **Python:** `def` with optional return `None`; Java requires declared return types and stricter path rules.
- **JavaScript:** functions are values; Java methods are not first-class until method references (Ch.04) and functional interfaces (later modules).

## 18. Revision and Mastery

**You should already understand:**

- How `main` orchestrates calls vs implements logic.
- Difference between printing inside a method vs returning a `String` for the caller to print.
- Why `static` helpers work without `new MethodsBasicsDemo()`.

**Common gaps:**

- Confusing **parameter** vs **argument** in code review.
- Thinking Java passes variables by reference (it does not—see Ch.03).
- Forgetting that **only return type** cannot overload a method.

**Questions you should answer:**

1. Can a `void` method use `return;` mid-body? (Yes.)
2. Must every branch in `describeScore` return if the method is `String`? (Yes, or the compiler rejects it.)
3. Why is `public static void main(String[] args)` fixed for default launches?

**Practical check:** Refactor a 40-line `main` into at least three named methods without changing behavior.

## 19. Exercises

### Beginner

1. Run `MethodsBasicsDemo` and trace which method runs for each output line.
2. Add `static int doubleValue(int x) { return x * 2; }` and print `doubleValue(21)` from `main`.
3. Write `static boolean isPassing(int score)` returning `score >= 50`; use it from `describeScore` (refactor).

### Intermediate

4. Implement `static int min(int a, int b)` and use it inside `clamp` (still three comparisons total or compose `max`/`min`—discuss readability).
5. Write a method that returns `"invalid"` when `min > max`, otherwise delegates to `clamp`.
6. Extract a `static void reportScore(int score)` that prints one line using `describeScore`.

### Advanced

7. Without running, predict output if `greet(null)` is called—then try and handle `null` safely.
8. Sketch how many stack frames exist when `main` calls `clamp` that calls nothing else (one sentence; full picture in Ch.03).

## 20. Interview Questions

1. **What is the difference between a method and a constructor?** — Constructors initialize objects; methods are ordinary operations (Module 05).
2. **Can you overload only by return type?** — No (Chapter 02).
3. **What does `void` mean?** — No return value to the expression that called the method.
4. **Is Java pass-by-reference?** — No; pass-by-value (references copied by value).

## 21. Solutions

1. Order: `greet` → `sum` → two `describeScore` → two `clamp`; each `println` evaluates its method call first.
2. Add method; `System.out.println(doubleValue(21));` prints `42`.
3. Example: `return isPassing(score) ? "passing" : "failing";` with `isPassing` as specified.
4. `min` mirrors `max` pattern; `clamp` can use `Math.min(Math.max(value, min), max)` once you trust `min <= max`.
5. Guard at top: `if (min > max) return "invalid";` then existing logic (return type becomes `String`—adjust callers).
6. `reportScore` prints `"score " + score + ": " + describeScore(score)`.
7. `greet(null)` prints `Hello, null!` without a null check; fix with guard returning early or using `"guest"`.
8. Two frames: `main` and `clamp` (when `clamp` is active, `main` waits)—simplified; JVM also has bootstrap frames.

## 22. Chapter Summary

Methods name reusable behavior: declaration (modifiers, return type, name, parameters, body), call sites (arguments), and `return` semantics separate `void` actions from value-producing helpers. Guard clauses keep validation readable. Demos use `static` methods; instance methods and pass-by-value details come in the next chapters of this module.

## 23. Key Terminology

Method · parameter · argument · return type · `void` · guard clause · local variable · invoke · `static` (preview)

## 24. References and Further Reading

- [JLS §8.4 — Method Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4)
- [JLS §6.3 — Scope of a Declaration](https://docs.oracle.com/javase/specs/jls/se21/html/jls-6.html#jls-6.3)
- [JLS §15.12 — Method Invocation Expressions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.12)
- `java.lang.Math` — library of `static` math helpers (contrast with your own methods)

## 25. Next Steps

- [02-overloading-varargs-and-recursion.md](02-overloading-varargs-and-recursion.md) — same name, different parameter lists; varargs; recursion.
- [03-pass-by-value-and-stack-frames.md](03-pass-by-value-and-stack-frames.md) — argument copying and call stacks.
- [04-static-instance-and-method-references.md](04-static-instance-and-method-references.md) — `static` vs instance in depth.

## Review Checklist

- [x] Learning objectives covered
- [x] Depth gate passed; progressive §7 examples
- [x] MethodsBasicsDemo verified on Temurin JDK 21.0.12.1
- [x] Revision and Mastery, exercises, solutions, JLS references
- [x] Instance/static and pass-by-value previewed without deferring core method teaching
