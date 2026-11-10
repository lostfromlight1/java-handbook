# API Design with Interfaces

> **Status:** Completed  
> **Module:** 08 — Interfaces and Abstraction  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Apply the **Dependency Inversion Principle**: depend on **abstractions**, not concretes.
- Design **service + repository** seams with interfaces for testability and swapping implementations.
- Explain how **constructors** and **factory** methods accept interface types.
- Sketch **fake** or **in-memory** implementations for unit tests.
- Evolve interfaces with **`default`** methods without breaking implementors (trade-offs).

## 2. Prerequisites

- **Ch.01–03** — interfaces, defaults, functional interfaces.
- **Module 05 Ch.04** — composition.
- **Module 06** — constructors and immutability patterns.

Brief refresher: **composition** places collaborators in fields (`private final UserRepository repo`). Typing those fields as **interfaces** lets production use JDBC/HTTP storage while tests use **in-memory** fakes without changing `UserService` logic.

## 3. Why This Concept Exists

Application code rots when **high-level policy** (`UserService.register`) imports **low-level details** (`new JdbcUserRepository()` inside every method). Changing database vendor or testing registration rules requires editing core logic.

Interfaces at boundaries express **roles** collaborators play. The **service** depends on **`UserRepository`** contract; **wiring** (Spring, manual `main`, tests) chooses the implementation.

This is the heart of maintainable **hexagonal** / **ports and adapters** thinking without requiring a framework.

## 4. Conceptual Explanation

### 4.1 Dependency Inversion Principle (DIP)

| Layer | Should know about |
|-------|-------------------|
| Domain / service | Interface operations it needs |
| Infrastructure | Concrete drivers implementing interfaces |
| `main` / config | Which concrete impl to construct |

**Inversion** means source code dependencies point **toward abstractions**, while control still flows top-down at runtime.

### 4.2 Repository interface pattern

```java
interface UserRepository {
    boolean exists(String username);
    void save(String username);
    String find(String username);
}
```

**Contract:**

- `exists` — answers without throwing for missing user (demo uses `null` stored field).
- `save` — persists username (demo overwrites single slot).
- `find` — returns stored name or `null`.

**Semantic** promises belong in team docs: e.g. `save` is **idempotent** on same username vs must reject duplicates—demo allows overwrite via `save` after `exists` check in service.

### 4.3 Service depending on interface

```java
class UserService {
    private final UserRepository repo;

    UserService(UserRepository repo) {
        this.repo = repo;
    }

    String register(String username) {
        if (repo.exists(username)) {
            return "exists:" + username;
        }
        repo.save(username);
        return "created:" + username;
    }
}
```

Constructor accepts **`UserRepository`**, not `InMemoryUserRepository`. Registration policy stays isolated from storage mechanics.

### 4.4 Wiring and testing

```java
UserRepository repo = new InMemoryUserRepository();
UserService service = new UserService(repo);
```

Tests use the same constructor with a **fake** recording calls or throwing controlled failures. Production wiring might use:

```java
UserRepository repo = new JdbcUserRepository(dataSource);
```

No `UserService` change.

### 4.5 Interface granularity (Interface Segregation)

Avoid **fat** `MegaRepository` with 40 methods when `UserService` needs three. Split `UserRepository`, `AuditLog`, `PasswordHasher` so implementors are not forced to stub unused operations.

### 4.6 Evolution with `default`

Adding `default boolean saveIfAbsent(String u)` can extend SPI with less breakage than new abstract methods. Prefer **abstract method** only when every implementation **must** consciously support behavior.

## 5. Terminology

| Term | Meaning |
|------|---------|
| **DIP** | High-level modules depend on abstractions; details implement them |
| **Port** | Interface representing incoming/outgoing interaction (hexagonal) |
| **Adapter** | Concrete implementation of a port (`InMemoryUserRepository`) |
| **Seam** | Boundary where behavior can be replaced (interface-typed field) |
| **Fake / stub** | Test implementation of an interface |

## 6. Syntax and Language Rules

Constructor injection sketch:

```java
public final class OrderService {
    private final PaymentProcessor payments;
    private final OrderRepository orders;

    public OrderService(PaymentProcessor payments, OrderRepository orders) {
        this.payments = Objects.requireNonNull(payments);
        this.orders = Objects.requireNonNull(orders);
    }
}
```

- **`final`** fields encourage immutable wiring after construction.
- **`Objects.requireNonNull`** fails fast on missing collaborators.

## 7. Step-by-Step Examples

### 7.1 Module demo

Full code: [api-interfaces-demo](../examples/core-java/api-interfaces-demo/).

**Flow:**

1. `register("ada")` — not exists → `save` → `created:ada`.
2. Second `register("ada")` — `exists` → `exists:ada`.
3. `find("ada")` returns stored username string `ada`.

**Verified output:**

```text
created:ada
exists:ada
find=ada
```

### 7.2 Realistic: multiple implementations

| Implementation | Use |
|----------------|-----|
| `InMemoryUserRepository` | Tests, prototypes |
| `JdbcUserRepository` | Production RDBMS |
| `CachingUserRepository` implements `UserRepository` wrapping delegate | Cross-cutting cache |

Decorator pattern preserves `UserService` dependency on `UserRepository`.

### 7.3 Advanced: package-private API surface

Expose **interface** in `api` package; keep `JdbcUserRepository` in `internal` package. Module system (Java 9+) can export only API package—interfaces document **published** contract.

## 8. Real-World Use Cases

- **Spring** — `@Autowired UserRepository` resolves bean implementing interface.
- **JDBC** — `DataSource` interface hides connection pool vendor.
- **Logging** — `Logger` / SLF4J facades.
- **HTTP clients** — interface for OkHttp vs JDK client vs mocks.

## 9. Internal Behavior

Interfaces compile to separate `.class` files. DI frameworks use reflection or bytecode generation to inject implementors—outside language, but motivated by same seams.

## 10. Edge Cases and Limitations

- **Leaky abstractions** — interface exposes SQL strings, defeating purpose.
- **God interface** — forces partial stubs (`UnsupportedOperationException`).
- **Circular dependencies** — fix package layering, not mega-interface.
- **Primitive obsession** — sometimes `record` + pure functions beats interface overload.

## 11. Common Mistakes

### Incorrect: `new` concrete inside service

```java
class UserService {
    private final UserRepository repo = new JdbcUserRepository(); // hard to test
}
```

### Corrected

Constructor-injected `UserRepository`.

### Incorrect: returning implementation type from API

```java
public JdbcUserRepository getRepo() { ... }
```

### Corrected

Return `UserRepository` or domain types only.

### Incorrect: interface mirrors entire JDBC API

Keep repository focused on domain verbs: `findById`, not `ResultSet` exposure.

## 12. Best Practices

- **Constructor injection** for required dependencies (immutability).
- Name interfaces for **caller** needs (`UserRepository`), not technology (`PostgresStuff`).
- Co-locate **contract tests** verifying each adapter honors semantics.
- Document **null** and **error** behavior on interface methods.
- Keep **framework annotations** off domain interfaces when possible.

## 13. Design Trade-offs

| Pure interface seam | Concrete class + inheritance |
|---------------------|------------------------------|
| Easy swap/mock | Shared code in base class |
| More types/files | Tighter hierarchy coupling |

| Manual wiring in `main` | DI framework |
|-------------------------|--------------|
| Explicit, no magic | Scales in large apps |

## 14. Performance Considerations

One interface call on repository is negligible vs database I/O. Avoid “remove interfaces for speed” without measurements.

## 15. Security Considerations

Interfaces do not imply **trust**. `UserRepository` implementors must **parameterize SQL**, enforce **authorization** at service layer, and never expose internal keys through `find` unless callers are trusted.

## 16. Java Version and Compatibility Notes

- Pattern unchanged across Java versions; **records** as DTOs pair well with interface services (Module 06).
- **Sealed** interfaces (Module 07) for closed result types in service returns.

## 17. Related Language Comparisons

Go encourages small interfaces discovered by implementors; Java favors **explicit** `implements` at declaration site—refactoring tools highlight breaking changes earlier.

## 18. Revision and Mastery

- **Gaps:** service still imports JDBC driver classes; repository interface returning JDBC types.
- **Advanced:** hexagonal mapping; contract tests; optional `default` migration on public SPI.
- **You should answer:** What does DIP invert? Why inject `UserRepository`?

## 19. Exercises

### Beginner

1. Add `delete(String username)` to repository interface and stub in memory.
2. Predict third line if demo calls `find("bob")` before register.

### Intermediate

3. Write `CachingUserRepository` delegating to another `UserRepository`.
4. Add unit test sketch asserting second `register` does not call `save` twice (fake with counter).

### Advanced

5. Compare interface repository vs Spring Data generated repository—benefits and coupling.

## 20. Interview Questions

1. What is DIP in one sentence?
2. Why constructor injection with interfaces?
3. Interface Segregation example?
4. How do defaults help public SPI evolution?
5. Difference between fake and mock?

## 21. Solutions

1. Clear `stored` when matches; service methods call as needed.
2. `find=missing` (`null` path returns `"missing"` string in demo `find` method).
3. Delegate `exists/save/find` with cache map layered on delegate.
4. Fake increments `saveCount`; assert `saveCount == 1` after two register attempts.
5. Spring Data speeds CRUD but ties to framework magic; hand-written interface keeps pure Java domain.

## 22. Chapter Summary

**API design** places **interfaces** at collaboration boundaries so **services** express policy while **adapters** handle details. Constructor wiring with **`UserRepository`** enables **in-memory** tests and production swaps without editing `UserService`. Module 08 completes interface mechanics from **contract** through **defaults**, **lambdas**, and **architecture**.

## 23. Key Terminology

DIP · port · adapter · seam · repository interface · Interface Segregation

## 24. References and Further Reading

- [SOLID — Dependency Inversion](https://en.wikipedia.org/wiki/Dependency_inversion_principle)
- Effective Java — Item on interface types over implementation types
- *Growing Object-Oriented Software, Guided by Tests* — mock seams

## 25. Next Steps

- [09-encapsulation-and-design/](../09-encapsulation-and-design/README.md) — SOLID and packaging
- Module 16 — functional streams building on SAM types

## Review Checklist

- [x] DIP, repository, service demo verified
- [x] Depth gate met
- [x] ApiInterfacesDemo Temurin 21.0.12.1
