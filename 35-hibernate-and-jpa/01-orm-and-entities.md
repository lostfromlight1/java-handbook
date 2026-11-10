# ORM and Entities

> **Status:** Completed  
> **Module:** 35 — Hibernate and JPA  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1; Hibernate 6.6.4.Final; H2 2.3.232

## 1. Learning Objectives

- Explain **ORM** mapping objects to relational tables.
- Annotate a **JPA entity** with `@Entity`, `@Id`, `@GeneratedValue`.
- Bootstrap **`EntityManagerFactory`** from `persistence.xml`.

## 2. Prerequisites

- Module **33–34** — JDBC and SQL.
- Module **06** — equals/identity; Module **15** records vs entities.

## 3. Why This Concept Exists

Hand-written JDBC mapping for dozens of tables is repetitive. **JPA** (Jakarta Persistence API) defines a **portable** object-relational mapping model; **Hibernate** is the dominant implementation. Spring Data JPA (Module **36+**) adds repositories on top — understand JPA first.

## 4. Conceptual Explanation

| Piece | Role |
|-------|------|
| **Entity** | Persistent domain class (mutable, identity) |
| **EntityManager** | Persistence context API (`persist`, `find`, `merge`, `remove`) |
| **Persistence unit** | Named config in `META-INF/persistence.xml` |
| **Provider** | Hibernate, EclipseLink — executes SQL |

**Entity state (JPA):**

- **Transient** — new, not associated with persistence context.
- **Managed** — tracked; changes flush to DB.
- **Detached** — was managed, context closed.
- **Removed** — scheduled for delete.

@Entity != record: entities have lifecycle callbacks, lazy proxies, and identity semantics across sessions.

## 5. Step-by-Step Examples

Demo: [jpa-h2-demo](../../examples/backend/jpa-h2-demo/).

Entity (table name avoids H2 reserved word `User`):

```java
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    // ...
}
```

**Verified output:**

```text
name=Lin
idNotNull=true
jpaH2=ok
```

`Persistence.createEntityManagerFactory("handbookPU")` reads `persistence.xml` JDBC URL `jdbc:h2:mem:jpa`.

## 6. Internal Behavior (Hibernate)

Hibernate generates SQL INSERT on `persist` + `commit`; uses **identity** column for `GenerationType.IDENTITY`. SQL logged when `hibernate.show_sql=true` (demo keeps false for clean output).

## 7. Real-World Use Cases

- CRUD services before REST exposure.
- Mapping legacy tables with `@Column(name = "legacy_col")`.

## 8. Edge Cases

- **Reserved SQL identifiers** — use `@Table(name = "app_user")` as demo shows.
- No-arg constructor required (JPA spec).
- `equals`/`hashCode` — use business key or id only when detached (advanced).

## 9. Common Mistakes

### Incorrect

Uses Lombok `@Data` on entity with lazy `@ManyToOne` — breaks `hashCode` with uninitialized proxies.

### Corrected

Careful equals/hashCode or exclude associations.

## 10. Chapter Summary

JPA entities map objects to tables; Hibernate executes SQL. Demo persists `Lin` with generated id on embedded H2.

## 11. Next Steps

[02-relationships-and-fetching.md](02-relationships-and-fetching.md)

## Review Checklist

- [x] Verified jpa-h2-demo output
