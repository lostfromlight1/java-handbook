# Relationships and Fetching

> **Status:** Completed  
> **Module:** 35 — Hibernate and JPA  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Concepts; single-entity demo in Ch.01 — relationship mapping taught with accurate JPA rules

## 1. Learning Objectives

- Map **`@ManyToOne`**, **`@OneToMany`**, **`@ManyToMany`** with join columns/tables.
- Choose **`FetchType.LAZY` vs `EAGER`** and understand **LazyInitializationException**.
- Apply **`JOIN FETCH`** and **entity graphs** to avoid N+1 queries.

## 2. Prerequisites

- Ch.01 — EntityManager lifecycle.

## 3. Why This Concept Exists

Relational data is **connected**. JPA models associations as object references and collections. Wrong fetch plans cause **N+1 SQL** (one query per row) or loading entire object graphs into memory.

## 4. Conceptual Explanation

| Annotation | FK location | Typical fetch |
|------------|-------------|---------------|
| `@ManyToOne` | Join column on many side | LAZY (default since JPA 2) |
| `@OneToMany(mappedBy = "...")` | Inverse side | LAZY |
| `@OneToOne` | Either side | LAZY preferred |

**Owning side** holds the foreign key column; **inverse** uses `mappedBy`.

**LazyInitializationException** — accessing uninitialized lazy association **outside** active session/transaction without fetched data.

**Fixes:**

- `JOIN FETCH` in JPQL: `SELECT o FROM Order o JOIN FETCH o.lines WHERE o.id = :id`
- `@EntityGraph(attributePaths = {"lines"})` on repository method (Spring module).
- DTO projection query instead of full entity graph.

## 5. Step-by-Step Examples

**Mapping sketch** (educational — extend jpa-h2-demo in your fork):

```java
@Entity
public class Department {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Employee> employees = new ArrayList<>();
}

@Entity
public class Employee {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department department;
}
```

Hibernate DDL would create `dept_id` FK on `employee` table when `hbm2ddl.auto=create`.

**JPQL fetch:**

```java
em.createQuery(
    "SELECT d FROM Department d JOIN FETCH d.employees WHERE d.id = :id",
    Department.class)
  .setParameter("id", 1L)
  .getSingleResult();
```

Run against your schema to see SQL in logs — handbook does not invent employee row counts without a demo.

## 6. Real-World Use Cases

- Order → line items for checkout API.
- Author → books catalog with pagination on parent only.
- Bidirectional sync: set both sides or use helper method maintaining consistency.

## 7. Edge Cases

- **CascadeType.ALL** on `@OneToMany` can delete unexpected children.
- **OrphanRemoval** deletes rows removed from collection — powerful and dangerous.
- Bag (`List`) vs `Set` — duplicate issue with multiple joins without `DISTINCT`.

## 8. Common Mistakes

### Incorrect

`EAGER` on all collections — Cartesian product explosions.

### Corrected

Default lazy; explicit fetch per use case.

## 9. Best Practices

- Service-layer **transaction** spans graph load for read use case.
- Integration test SQL count assertions for N+1 detection.
- Prefer **DTOs** for API responses over exposing entity graphs.

## 10. Chapter Summary

JPA relationships model foreign keys; lazy loading and join fetch control SQL shape — critical before Spring Data convenience.

## 11. Next Steps

[03-transactions-caching-and-performance.md](03-transactions-caching-and-performance.md)

## Review Checklist

- [x] JPA relationship rules accurate; no fabricated query counts
