# Creational Patterns

> **Status:** Completed  
> **Module:** 41 — Design Patterns  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 — [creational-demo](../../examples/patterns/creational-demo/)

## 1. Learning Objectives

- Apply **factory method**, **abstract factory**, **builder**, and **singleton** (cautiously) in Java.
- Prefer **constructors/factories** over excessive pattern ceremony.
- Relate factories to Spring `@Bean` methods (composition root creates objects).

## 2. Prerequisites

- Ch.01 — SOLID, especially D and O.
- Module 06 — constructors, static factories.

## 3. Why This Concept Exists

Object creation sprawls (`new` everywhere) hides **which implementation** runs and complicates testing. **Creational patterns** centralize creation while keeping callers on stable types.

## 4. Patterns

### Factory method

Subclass or factory class decides implementation:

```java
Exporter ex = ExporterFactory.create(ExportKind.CSV);
```

### Abstract factory (conceptual)

Family of related products (`WidgetFactory` → `Button` + `Checkbox`). Use when you must swap whole UI kits; otherwise YAGNI.

### Builder

Step-by-step construction for many optional fields:

```java
Report report = Report.builder().title("T").rowCount(2).build();
```

Java **records** and compact constructors cover small cases; builders help when validation chains grow.

### Singleton

One instance per JVM (`enum` idiom). **Danger:** global state, testing pain. Prefer **scoped singleton** in DI container over hand-rolled `getInstance()`.

## 5. Runnable demo

[creational-demo](../../examples/patterns/creational-demo/)

**Verified output:**

```text
factory=csv:rows
builder=Report{title=T,rows=2}
creational=ok
```

## 6. Spring connection (conceptual)

`@Bean` methods are **factory methods** in the composition root—container manages singleton scope.

## 7. Real-World Use Cases

- `ExporterFactory` by file format.
- `HttpClient` builder (JDK) for timeouts/TLS.
- Test fixtures building complex domain aggregates.

## 8. Common Mistakes

### Incorrect — singleton for every service

Use DI lifecycle instead.

### Incorrect — builder for two-field immutable type

Use record.

## 9. Revision and Mastery

- Factory method vs abstract factory?
- Why is enum singleton preferred over double-checked locking?

## 10. Exercises

1. Add `JSON` exporter to demo factory without changing `Checkout`.
2. When would you use `Optional` factory vs exception?
3. Map Spring `@Bean` to pattern name.

## 11. Chapter Summary

Creational patterns concentrate object birth decisions; the demo shows factory and builder plainly. Spring `@Bean` registration is enterprise-scale factory wiring at the root.

## 12. Next Steps

[03-structural-and-behavioral-patterns.md](03-structural-and-behavioral-patterns.md)

## Review Checklist

- [x] Verified creational-demo; singleton warning
