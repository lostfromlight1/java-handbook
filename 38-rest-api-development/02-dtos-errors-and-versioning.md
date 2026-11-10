# DTOs, Errors, and Versioning

> **Status:** Completed  
> **Module:** 38 — REST API Development  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Separate **API DTOs** from **persistence entities** (Module 35).
- Design **stable error envelopes** (RFC 7807 Problem Details).
- Version APIs via **URL**, **header**, or **media type** with trade-offs.
- Handle **optimistic locking** and conflict responses.

## 2. Prerequisites

- Ch.01 — REST semantics.
- Module 35 — entities, lazy loading.

## 3. Why This Concept Exists

Clients depend on **field names and error shapes**. Exposing JPA entities ties HTTP to database schema and risks lazy-load serialization failures. **Versioning** lets you ship breaking changes without stranding mobile apps on old builds.

## 4. DTOs

```java
public record UserDto(long id, String name, String email) {
    public static UserDto from(User entity) {
        return new UserDto(entity.getId(), entity.getName(), entity.getEmail());
    }
}
```

| Layer | Responsibility |
|-------|----------------|
| Entity | Persistence, associations, lifecycle |
| DTO | Wire format, validation for API |
| Domain model | Optional rich logic (Module 40 DDD) |

**Mapping:** MapStruct or manual mappers in the service layer—keep controllers thin.

### Incorrect — return entity with lazy `orders` collection

Jackson triggers extra queries or `LazyInitializationException`.

### Corrected — `UserSummaryDto` without collections or use explicit `@EntityGraph` in service.

## 5. Error responses

Standard shape (Spring 6 `ProblemDetail`):

```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "email must be a well-formed address",
  "instance": "/api/v1/users"
}
```

| Field | Purpose |
|-------|---------|
| `type` | URI identifying problem category |
| `title` | Short summary |
| `status` | HTTP code duplicate |
| `detail` | Safe human message |
| `instance` | Request path |

**Field errors** for validation: extend with `errors: [{field, message}]` in a custom subtype—document for clients.

**Security:** map internal exceptions to generic 500; log stack server-side only.

## 6. Versioning strategies

| Strategy | Example | Pros | Cons |
|----------|---------|------|------|
| URI prefix | `/api/v1/users` | Obvious, easy routing | URL proliferation |
| Header | `Accept-Version: 2` | Clean URLs | Hidden from browsers |
| Media type | `Accept: application/vnd.handbook.v2+json` | Fine-grained | Complex clients |

**Backward compatible changes:** add optional fields, new endpoints.  
**Breaking changes:** new major version; deprecate with `Sunset` header and timeline.

## 7. Optimistic concurrency

Entity with `@Version` column; on stale update Hibernate throws `OptimisticLockException` → map to **409 Conflict** with problem detail explaining stale resource.

## 8. Real-World Use Cases

- Mobile apps pinned to `v1` while web uses `v2`.
- Public partner API with published deprecation policy.
- GDPR: DTO omits internal flags.

## 9. Common Mistakes

### Incorrect — identical DTO for admin and public APIs

Leaks internal fields.

### Incorrect — versioning every typo fix as v2

Reserve major bumps for contract breaks.

## 10. Revision and Mastery

- Why are DTOs preferable to `@JsonView` on entities for new projects?
- 409 vs 400 for duplicate key?

## 11. Exercises

1. Define error JSON for validation of three fields.
2. Plan migration from v1 to v2 for renaming a field.
3. Write mapper from `Order` entity to `OrderSummaryDto` without lazy collections.

## 12. Chapter Summary

DTOs stabilize API contracts; problem details unify errors; versioning policies manage change. Entities stay inside the transaction boundary.

## 13. Next Steps

[03-openapi-and-api-testing.md](03-openapi-and-api-testing.md)

## Review Checklist

- [x] DTO/entity split; ProblemDetail; versioning table
