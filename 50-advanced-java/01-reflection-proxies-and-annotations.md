# Reflection, Proxies, and Annotations

> **Status:** Completed  
> **Module:** 50 — Advanced Java  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 — [reflection-annotations-demo](../../examples/advanced/reflection-annotations-demo/)

## 1. Learning Objectives

- Use **reflection** to inspect types and invoke members with correct exception handling.
- Contrast **compile-time** types with **runtime** `Class` metadata.
- Build **JDK dynamic proxies** and relate them to Spring AOP (Module 36).
- Define **annotations** with retention and process them at compile time vs runtime.

## 2. Prerequisites

- **Modules 05–08** — classes, inheritance, interfaces.
- **Module 36** — `Proxy`, `InvocationHandler` preview in AOP chapter.

## 3. Why This Concept Exists

Frameworks (Spring, JPA, JUnit) discover behavior from **metadata** instead of hard-coded lists: which methods are endpoints, which fields are columns, which tests to run. **Reflection** reads that metadata at runtime; **annotations** attach declarative markers; **proxies** intercept interface calls for cross-cutting logic without editing every business class.

## 4. Conceptual Explanation

### Reflection API core

| Type | Examples |
|------|----------|
| **Class<?>** | `String.class`, `obj.getClass()` |
| **Method** | `getMethod`, `invoke` |
| **Field** | `getDeclaredField`, `setAccessible` (security manager / modules may restrict) |
| **Constructor** | `getConstructor().newInstance()` |

**invoke** can throw `InvocationTargetException` — unwrap `getCause()` for real failure.

### Runnable demo

[reflection-annotations-demo](../../examples/advanced/reflection-annotations-demo/):

```bash
javac ReflectionAnnotationsDemo.java
java ReflectionAnnotationsDemo
```

**Verified (Temurin 21.0.12.1):** `hasTimed=true`; `result=Hello, Lin`; `reflectionDemo=ok`

`@Retention(RetentionPolicy.RUNTIME)` makes `@Timed` visible to `isAnnotationPresent`.

### JDK dynamic proxies

Only **interfaces** can be proxied with `java.lang.reflect.Proxy`. See [jdk-dynamic-proxy-demo](../../examples/backend/jdk-dynamic-proxy-demo/).

| Approach | Requirement |
|----------|-------------|
| JDK proxy | Target implements interface(s) |
| CGLIB (Spring) | Subclass concrete class; no `final` |

### Annotations

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audited {
    String value() default "";
}
```

| Retention | Visible when |
|-----------|--------------|
| `SOURCE` | Compiler only (e.g. `@Override`) |
| `CLASS` | Bytecode, not runtime reflection |
| `RUNTIME` | Reflection, frameworks |

**Repeating** and **container** annotations exist for multiple markers on one element (Java 8+).

## 5. Internal Behavior (JVM)

Reflection objects cache metadata; first reflective call can be slower (security checks, inflation to generated accessors on HotSpot for frequent `Method.invoke`).

**JPMS:** deep reflection on non-exported packages requires `--add-opens` (Module 29 JPMS). Frameworks document required opens for JDK 21.

## 6. Real-World Use Cases

- Spring **component scan** and `@Autowired` injection.
- JPA attribute mapping via field annotations.
- Jackson JSON property naming via annotations.
- Test frameworks discovering `@Test` methods.

## 7. Edge Cases

- **Generics erasure** — `List<String>` appears as `List` at runtime; use `ParameterizedType` when needed.
- **SecurityManager** (deprecated/removal path) — fewer deployments; still know access checks can fail.
- **Records** — reflection exposes canonical constructor and accessors (Module 15).

## 8. Common Mistakes

### Incorrect — reflection in hot paths without caching

Re-resolving `Method` every request adds overhead.

### Corrected — cache `MethodHandle` or Spring metadata once.

### Incorrect — swallowing `InvocationTargetException`

Hides real NPE inside invoked method.

### Corrected — unwrap and rethrow cause.

## 9. Design Trade-offs

| Explicit wiring | Reflection/DI |
|-----------------|---------------|
| Compile-time clarity | Framework ergonomics |
| Refactoring safe in IDE | Runtime discovery failures |

## 10. Revision and Mastery

- Why must interface proxy target implement an interface?
- Difference `RetentionPolicy.CLASS` vs `RUNTIME`?
- Link to Spring self-invocation proxy issue (Module 36).

## 11. Exercises

1. Reflectively invoke `Integer.parseInt` and handle `InvocationTargetException`.
2. Add a runtime annotation and print it from `main`.
3. Re-run jdk-dynamic-proxy-demo and explain call order.

## 12. Chapter Summary

Reflection, annotations, and proxies underpin modern Java frameworks: metadata at runtime, interface interception for AOP, and retention policies that decide visibility. Prefer MethodHandles for heavy invocation (Ch.02); native image needs explicit reflection config (Module 49).

## Next Steps

[02-method-handles-varhandles-and-ffm.md](02-method-handles-varhandles-and-ffm.md)

## Review Checklist

- [x] Reflection; proxies; annotations; verified demo
