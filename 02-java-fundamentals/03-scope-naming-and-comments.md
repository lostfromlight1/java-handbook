# Scope, Naming, and Comments

> **Status:** Completed  
> **Module:** 02 — Java Fundamentals  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Oracle JDK 21.0.9

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Explain **scope**: where a name is visible.
- Use block scope and understand loop-variable scope.
- Apply Java naming conventions (classes, methods, variables, constants).
- Write useful comments without narrating obvious code.
- Avoid shadowing and confusing redeclarations.

## 2. Prerequisites

- [01-variables-and-types.md](01-variables-and-types.md)
- [02-operators-and-expressions.md](02-operators-and-expressions.md)

## 3. Why This Concept Exists

Most “I declared it but can’t use it” errors are scope. Most unreadable code is naming. Comments that lie are worse than silence. These soft skills are part of professional Java.

## 4. Conceptual Explanation

### 4.1 Scope

**Scope** is the region of a program where a declared name can be referenced.

Local variables are scoped to the **block** `{ ... }` that contains their declaration (and nested blocks inside it).

```java
int outer = 1;
{
    int inner = 2; // visible only in this block
}
// inner not visible here
```

**Method parameters** are locals too. Their scope is the entire method body from the opening `{` of the method through its closing `}`—the same region where you can use the parameter name.

```java
static int scale(int value, int factor) {
    // value and factor are in scope for the whole method
    return value * factor;
}
// value and factor are not visible here
```

You cannot reference a parameter outside its method. Inside the method, the parameter name is fixed for that invocation until the method returns.

### 4.2 Loop variables

```java
for (int i = 0; i < 2; i++) {
    // i visible here
}
// i not visible here
```

### 4.3 Shadowing

A name in an **inner** scope can **hide** a same-named variable in an **outer** scope. The inner declaration wins **inside** that block only. This is legal but easy to misread:

```java
int limit = 10;
{
    int limit = 5; // shadows outer limit inside this block
    System.out.println(limit); // prints 5
}
System.out.println(limit); // prints 10 again
```

Fields vs locals use the same idea (Module 05 uses `this.field` when a parameter shadows a field). Prefer distinct names (`maxRetries` vs `retries`) over clever shadowing.

### 4.3.1 Illegal duplicate locals

Two locals with the **same name** cannot occupy **overlapping** scopes where both are visible at once:

```java
void bad() {
    int count = 1;
    int count = 2; // compile error: variable count is already defined
}
```

Nested blocks may reuse a name only if the outer binding is **not** in scope (the inner block completely hides the outer name only when the inner declares its own `count`). Redeclaring in the **same** block is always illegal.

### 4.4 Naming conventions

| Kind | Convention | Example |
|------|------------|---------|
| Class / interface | `UpperCamelCase` | `PrimitivesDemo` |
| Method / variable | `lowerCamelCase` | `itemCount` |
| Constant (`final` static later) | `UPPER_SNAKE` | `MAX_SCORE` |
| Package | lowercase dotted | `com.example.app` |

Names should reveal intent: `secondsRemaining` beats `sr`.

### 4.5 Comments

```java
// Single-line comment

/*
 * Multi-line comment
 */

/**
 * Javadoc for APIs (Module 20 / style guides)
 */
```

A complete minimal Javadoc on a public method names the contract, not the implementation:

```java
/**
 * Returns a display label for the given user id, or {@code null} if unknown.
 *
 * @param userId non-negative account id
 * @return human-readable label, or {@code null}
 */
public String labelFor(long userId) {
    // ...
    return null;
}
```

`@param` documents each parameter; `@return` documents the result (omit `@return` only for `void` methods). Comment **why** and non-obvious constraints, not “increment i by 1.”

## 5. Terminology

| Term | Meaning |
|------|---------|
| Scope | Visibility region of a name |
| Block | `{ ... }` region |
| Shadowing | Inner name hides outer name |
| Convention | Community naming rules (not compiler law) |

## 6. Syntax and Language Rules

Two local variables with the same name cannot both be in scope at once in the same block (§4.3.1). Parameters are locals for the whole method body. Class members have different rules (Module 05).

## 7. Step-by-Step Examples

### 7.1 Scope demo

[examples/core-java/scope-demo/](../examples/core-java/scope-demo/)

**Verified output:**

```text
outer=1
inner=2
outer still visible=1
back to outer=1
loop i=0
loop i=1
```

### 7.2 Shadowing in a nested block

```java
int limit = 10;
{
    int limit = 2; // legal shadow; outer limit hidden here
    // use inner limit
}
// outer limit visible again
```

### 7.3 Naming contrast

```java
int a = 86400;                 // weak
int secondsPerDay = 86_400;    // clear
```

### 7.4 Comment contrast

```java
// Bad: add one to count
count++;

// Better: retry budget consumed after a failed remote call
retriesRemaining--;
```

## 8. Real-World Use Cases

Code reviews reject cryptic names; APIs need Javadoc; bugs hide in shadowed fields.

## 9. Internal Behavior

Scope is a language/compile-time concept. Bytecode locals use slots—interesting later, not required now.

## 10. Edge Cases and Limitations

- **`switch` scope:** In Java 14+, a `switch` label can introduce a block `{ ... }` so that a variable declared for one `case` does not leak into the next `case`. Without a block, `case` labels share one scope in older patterns—Module 03 shows the block form and fall-through rules.
- Resource variables in try-with-resources have special scope (Module 14).

## 11. Common Mistakes

### Incorrect

Using loop `i` after the loop.

### Corrected

Declare outside if you need the final value—or redesign.

### Incorrect

`int Class = 5;` / naming variables like types.

### Corrected

Follow camelCase conventions; avoid keywords.

## 12. Best Practices

- Smallest reasonable scope (declare near first use).
- Consistent glossary of domain terms.
- Delete comments that merely restate code.

## 13. Design Trade-offs

| Style | Benefit | Cost |
|-------|---------|------|
| Very short names | Fast typing | High reading cost |
| Very long names | Clear | Noise in dense formulas |
| Many comments | Context | Drift when code changes |

## 14. Performance Considerations

Naming/scope do not affect runtime speed meaningfully.

## 15. Security Considerations

Don’t leave secrets in comments or committed sample configs.

## 16. Java Version and Compatibility Notes

Block scope is long-standing. Validated example on **21.0.9**.

## 17. Related Language Comparisons

Python has function-level locals by default; Java’s block scope is stricter—don’t assume Python rules.

## 18. Revision and Mastery

Field vs local shadowing, module/package visibility, Javadoc `@param` contracts.

## 19. Exercises

### Beginner

1. Run ScopeDemo; try printing `inner` after its block and read the error.
2. Rename three weak variables in an old snippet to intentional names.

### Intermediate

3. Write a method with a parameter `count` and a local `count`—observe the compiler.
4. Add a Javadoc comment to a tiny public method stating its precondition.

### Advanced

5. Refactor a nested block to reduce scope of a temporary.
6. Review a classmate/open-source file for misleading comments.

## 20. Interview Questions

1. **What is scope?** — Where a name is visible.  
2. **Java class naming?** — UpperCamelCase.  
3. **Good comments explain?** — Why / constraints, not obvious what.  

## 21. Solutions

1. Compile error: cannot find symbol.  
3. Duplicate variable / shadowing error depending on placement.  

## 22. Chapter Summary

Scope limits visibility to blocks; naming conventions aid teamwork; comments capture intent that code cannot.

## 23. Key Terminology

Scope · block · shadowing · camelCase · Javadoc

## 24. References and Further Reading

- Oracle Java Code Conventions (historical) / modern team style guides
- Next: [04-type-conversion-and-inference.md](04-type-conversion-and-inference.md)

## 25. Next Steps

[04-type-conversion-and-inference.md](04-type-conversion-and-inference.md)

## Review Checklist

- [x] ScopeDemo verified
- [x] Depth gate passed
