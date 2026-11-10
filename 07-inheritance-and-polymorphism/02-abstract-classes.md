# Abstract Classes

> **Status:** Completed  
> **Module:** 07 — Inheritance and Polymorphism  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Declare **`abstract` classes** and **`abstract` methods** and explain when instantiation is illegal.
- Contrast **abstract classes** with **interfaces** and **concrete** classes at a design level (interfaces deepen in Module 08).
- Implement the **Template Method** pattern with a **`final`** algorithm skeleton and overridable hooks.
- Combine **`abstract`** types with concrete fields, constructors, and non-abstract methods.
- Choose abstract classes vs interfaces vs composition for shared behavior.

## 2. Prerequisites

- **Ch.01** — inheritance, `protected`, `final` methods, overriding.
- **Module 05 Ch.01** — classes, methods, fields.
- **Module 06 Ch.01** — constructors and `super(...)`.

Brief refresher: an **abstract method** has no body in the declaring class; a **concrete subclass** must implement it (unless that subclass is also abstract). You cannot write `new AbstractType()`—the compiler rejects instantiating abstract classes.

## 3. Why This Concept Exists

Some supertypes are **incomplete by design**: `Report` is not a report format—it is shared scaffolding. Subclasses supply **Pdf** vs **Csv** bodies. Without **`abstract`**, you might ship a concrete superclass with empty `body()` stubs that callers forget to override—runtime bugs instead of compile-time enforcement.

**Abstract classes** excel when:

- Several subclasses share **state** (title field, counters) and **code** (header/footer).
- You want a **fixed workflow** with customizable steps—**template method**.

**Interfaces** (Module 08) excel when multiple unrelated classes share a **contract** without shared fields. Many modern APIs lead with interfaces; abstract classes remain common in frameworks and document export pipelines.

## 4. Conceptual Explanation

### 4.1 What `abstract` means

| Kind | Can `new`? | Must implement missing methods? |
|------|------------|----------------------------------|
| Concrete class | Yes | N/A |
| Abstract class | No | Subclass must implement abstract methods (if concrete) |
| Abstract method | — | Declared in abstract (or concrete) class without body |

```java
abstract class Report {
    abstract String body();
}
```

Any **non-abstract** subclass must provide `body()` or be a compile error.

### 4.2 Partial implementation

Abstract classes may contain:

- **Fields** and **constructors** (initialize shared state).
- **Concrete methods** with full bodies (`header()`, `footer()`).
- **Abstract methods** subclasses must specialize (`body()`).
- **`final` methods** that must not change (`render()` orchestration).

This mixes **shared implementation** with **required specialization**—interfaces cannot hold instance state (except `default` methods without fields tied to instance in the same way—Module 08).

### 4.3 Template Method pattern

**Template method** = one **public or protected `final`** method defines the algorithm steps; **hooks** are overridden in subclasses.

In the demo, `render()` is **`final`**:

```java
final String render() {
    return header() + "\n" + body() + "\n" + footer();
}
```

Subclasses cannot skip the footer or reorder steps by overriding `render()`. They only fill `body()` (and may override `header()` if made overridable—in the demo `header` is concrete with optional override via `protected`).

Benefits:

- **DRY** shared logic.
- **Invariant order** enforced by `final`.
- New formats add a class, not copy-paste pipelines.

### 4.4 Abstract class vs interface (preview)

| Factor | Abstract class | Interface (Module 08) |
|--------|----------------|------------------------|
| Instance fields | Yes | No (only `public static final` constants historically) |
| Constructor | Yes | No instance ctor |
| Multiple inheritance | One class parent | Many interfaces |
| `abstract` methods | Yes | Implicitly abstract before `default` |
| Best when | Shared state + template | Capability plug-in across unrelated types |

From Java 8+, interfaces can have **`default` methods**—overlap increased. Rule of thumb: use **abstract class** when **shared mutable state** or a **rigid workflow** dominates; use **interface** for **capability** and testing seams.

### 4.5 Abstract + `sealed`

Java 17+ **sealed** abstract classes restrict permitted subclasses (Ch.04)—useful for fixed report families or AST node sets.

## 5. Terminology

| Term | Meaning |
|------|---------|
| **Abstract class** | Class declared `abstract`; cannot be instantiated |
| **Abstract method** | Method without body; subclass provides implementation |
| **Concrete subclass** | Non-abstract class that fills in abstract methods |
| **Template method** | `final` method defining algorithm with override hooks |
| **Hook method** | Overridable step called by template method |

## 6. Syntax and Language Rules

```java
abstract class Base {
    private final String title;
    Base(String title) { this.title = title; }

    final void run() {        // template
        step1();
        step2();
    }

    protected void step1() { }          // optional hook (default)
    protected abstract void step2();    // required hook
}

class Impl extends Base {
    Impl(String t) { super(t); }
    @Override protected void step2() { ... }
}
```

- Abstract class **may** have **zero** abstract methods—legal but rare; usually a marker or restriction via `protected` constructors.
- **Cannot** mark `static`, `private` (until Java 9+ private abstract is not allowed), or `final` methods as **abstract**.
- Subclass visibility when overriding **cannot be weaker**.

## 7. Step-by-Step Examples

### 7.1 Minimal abstract method

```java
abstract class Animal {
    abstract String speak();
}
class Dog extends Animal {
    @Override String speak() { return "woof"; }
}
```

### 7.2 Realistic: report rendering

Runnable: [abstract-classes-demo](../../../examples/core-java/abstract-classes-demo/).

**Verified output:**

```text
REPORT:Q1
pdf-body
END
REPORT:Q1
csv-body
END
```

`PdfReport` and `CsvReport` only implement `body()`. Shared title flows through `Report(String title)` and `header()` / `footer()`.

### 7.3 Advanced: mixed hooks

```java
abstract class Parser {
    public final Result parse(InputStream in) throws IOException {
        validate(in);
        return doParse(in);
    }
    protected void validate(InputStream in) { /* default no-op */ }
    protected abstract Result doParse(InputStream in) throws IOException;
}
```

Subclasses may override `validate`; must implement `doParse`.

## 8. Real-World Use Cases

- **Spring** `AbstractController` / various `*Support` base classes (legacy patterns).
- **Servlet API:** `HttpServlet` with `doGet` / `doPost` hooks (similar template idea).
- **Export pipelines:** PDF/Excel/CSV share metadata headers.
- **JUnit 4** `TestCase` (legacy); JUnit 5 favors composition and extensions.
- **InputStream** hierarchy: abstract `read()` with shared `skip`, `close` helpers (JDK itself).

## 9. Internal Behavior

No special JVM opcode for abstract types—abstractness is **compile-time**. At runtime, objects are instances of **concrete** classes. Virtual dispatch on hooks behaves like normal overrides (Ch.03).

## 10. Edge Cases and Limitations

- **Constructor on abstract class** — still runs when subclass is constructed; abstract class can enforce invariants in ctor.
- **All methods abstract** — class is like an interface with single inheritance cost; consider interface instead.
- **Cannot instantiate anonymous subclass** missing abstract methods unless you implement them in the anonymous body.
- **Diamond issues** — still only one superclass; abstract does not change that.

## 11. Common Mistakes

### Incorrect: instantiating abstract class

```java
Report r = new Report("Q1"); // compile error
```

### Corrected

```java
Report r = new PdfReport("Q1");
```

### Incorrect: concrete class leaves abstract method unimplemented

```java
class Broken extends Report { } // compile error: body() missing
```

### Incorrect: overriding `final render()`

```java
class Evil extends Report {
    @Override final String render() { return "hacked"; } // compile error
}
```

## 12. Best Practices

- Keep template methods **`final`**; name hooks clearly (`buildBody`, `validateInput`).
- Minimize **`protected`** surface—only true extension points.
- Prefer **small abstract hierarchies**; export **interfaces** for public API when possible.
- Document which hooks **must** call `super` if base changes state.

## 13. Design Trade-offs

| Choice | Benefit | Cost |
|--------|---------|------|
| Abstract class base | Shared fields + template | Subclasses locked to one hierarchy |
| Interface + helper class | Flexible | Split between contract and code |
| All-concrete base with empty defaults | Easy start | Forgetting to override—runtime bugs |
| `final` template | Safe workflow | Subclasses cannot reorder for valid variant flows |

## 14. Performance Considerations

Abstract indirection is ordinary virtual calls. Template method often enables **single place** for logging/timing—cheap compared to duplicated pipeline code.

## 15. Security Considerations

If hooks run **untrusted subclass code** in a plugin architecture, the **`final` template** should perform authorization **before** hooks—not inside overridable methods only. Abstract base in **same security domain** as app is usually fine.

## 16. Java Version and Compatibility Notes

- Abstract classes since Java 1.0.
- **Sealed abstract classes** (17+)—Ch.04.
- **Records** cannot extend arbitrary classes—usually not abstract bases for records.

## 17. Related Language Comparisons

C++ **pure virtual** (`= 0`) parallels abstract methods. Python ABC module enforces abstract methods at instantiation time—Java enforces at **subclass compile** time for concrete children.

## 18. Revision and Mastery

- **Gaps:** thinking abstract class must be all abstract methods; forgetting constructor on abstract parent when subclass needs title.
- **Advanced:** when `default` interface methods replace abstract bases; mixing sealed + abstract for ASTs.
- **You should answer:** Why mark `render()` `final`? When is interface better than abstract class?

## 19. Exercises

### Beginner

1. Add `HtmlReport` to the demo with `body()` returning `html-body`.
2. Why does `new PdfReport("Q1")` compile but `new Report("Q1")` does not?

### Intermediate

3. Move header format to `protected abstract String header();` with default in concrete middle class—sketch class diagram.
4. Implement `abstract class Validator` with `final boolean validate(String s)` calling `checkNotBlank` (concrete) and `checkFormat` (abstract).

### Advanced

5. Compare template method here to **Strategy** pattern with composed `BodyRenderer` interface—one paragraph trade-off.

## 20. Interview Questions

1. Can an abstract class have a constructor?
2. Difference between abstract class and interface in Java 21?
3. What is the Template Method pattern?
4. Can abstract methods be `private`?
5. Why combine `abstract` with `final` on different methods?

## 21. Solutions

1. Same output pattern with third format block when run after pdf/csv.
2. `Report` is abstract—no complete implementation to instantiate; `PdfReport` is concrete.
3. Middle `TaggedReport extends Report` implements default header; `PdfReport extends TaggedReport`.
4. `validate` returns `checkNotBlank(s) && checkFormat(s)` with abstract `checkFormat`.
5. Template method fixes flow in inheritance; Strategy swaps behavior via field—favors composition, easier testing, no fragile base.

## 22. Chapter Summary

**Abstract classes** capture **shared state and workflow** while forcing subclasses to supply missing pieces. The **`final` template method** protects algorithm structure; **hooks** customize steps. The report demo shows **one abstraction, many outputs**. Next: **dynamic dispatch** and **Liskov Substitution**—why overrides must respect superclass promises.

## 23. Key Terminology

Abstract class · abstract method · concrete subclass · template method · hook method

## 24. References and Further Reading

- [JLS §8.1.1.2 — Abstract Class Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.1.1.2)
- [Template Method — Gang of Four](https://en.wikipedia.org/wiki/Template_method_pattern) (concept)
- Effective Java — Item on interfaces vs abstract classes

## 25. Next Steps

- [03-dynamic-dispatch-and-lsp.md](03-dynamic-dispatch-and-lsp.md)
- Module 08 — interfaces, `default` methods, multiple inheritance of type

## Review Checklist

- [x] Learning objectives covered
- [x] Template method + demo output verified
- [x] Depth gate met; abstract vs interface preview
- [x] AbstractClassesDemo on Temurin JDK 21.0.12.1
- [x] Exercises, Revision and Mastery, references
