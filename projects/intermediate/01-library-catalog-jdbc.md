# Project: Library Catalog (JDBC + H2)

> **Level:** Intermediate  
> **Status:** Completed  
> **Prerequisites:** Modules 12–14, 33–34, 30 (JUnit)  
> **Last reviewed:** 2026-11-10

## Goals

CRUD for books with JDBC against embedded H2; parameterized SQL; transaction for checkout that decrements availability.

## Learning objectives

- Schema migration via SQL script in `resources/schema.sql`.
- `PreparedStatement` for all user input.
- `Connection` commit/rollback on checkout failure.
- Repository pattern separating SQL from CLI or thin REST-less menu.

## Architecture

```text
LibraryApp → BookService → JdbcBookRepository → H2
```

| Table | Columns |
|-------|---------|
| `books` | `id`, `isbn`, `title`, `author`, `copies_available` |

## Milestones

1. Schema + list all books.
2. Add book (admin command).
3. Checkout by isbn — transaction: verify available → decrement.
4. Integration test with in-memory H2 (Module 33 pattern).

## Acceptance criteria

- [ ] No string-concatenated SQL with user values.
- [ ] Checkout on 0 copies rolls back with domain error.
- [ ] Tests run with `mvn -q test` on Temurin 21.
- [ ] README documents JDBC URL `jdbc:h2:file:./data/library`.

## Starter hints

- Copy structure from [h2-jdbc-demo](../../examples/backend/h2-jdbc-demo/).
- Use try-with-resources on `Connection`, `PreparedStatement`, `ResultSet`.

## Verification steps

```bash
./mvnw -q test
java -jar target/library-catalog.jar checkout 9780321356680
```

Second checkout same isbn when one copy → failure message, count unchanged.

## Security

Single-user CLI; if adding HTTP later, revisit SQL injection and auth.

## Extension

Full-text search with `LIKE`; Flyway for migrations (Module 34 theme).
