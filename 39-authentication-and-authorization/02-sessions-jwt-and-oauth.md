# Sessions, JWT, and OAuth

> **Status:** Completed  
> **Module:** 39 — Authentication and Authorization  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11
> **Chapter profile:** Technical
> **Depth target:** Levels 2–3

**Section policy:** Omissions recorded in Review Checklist.

## 1. Learning Objectives

- Compare **server-side sessions** and **stateless JWT** trade-offs.
- Read JWT structure (**header**, **payload**, **signature**) without treating it as encryption.
- Outline **OAuth 2.0** roles and **OpenID Connect** identity layer.
- Spot JWT pitfalls: algorithm confusion, long TTL, stolen refresh tokens.

## 2. Prerequisites

- Ch.01 — AuthN/AuthZ vocabulary.
- Module 32 — TLS.

## 3. Why This Concept Exists

Browser apps traditionally used **session cookies**; distributed APIs favor **Bearer tokens**. **OAuth** delegates authorization to identity providers (Google, corporate IdP) so your app does not store corporate passwords.

## 4. Server-side sessions

1. User posts credentials to `/login`.
2. Server validates, creates session record (memory, Redis, DB).
3. Client stores **session id** in **HttpOnly**, **Secure** cookie.
4. Each request sends cookie; server loads session → `Principal`.

| Pros | Cons |
|------|------|
| Easy revocation (delete session) | Stateful; sticky sessions or shared store |
| Smaller cookie | CSRF requires token sync (Spring CSRF for cookie apps) |

**Session fixation defense:** rotate session id after successful login.

## 5. JWT (JSON Web Token)

Three Base64url segments joined by `.`:

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0In0.signature
```

| Part | Content |
|------|---------|
| Header | Alg (`HS256`, `RS256`), type `JWT` |
| Payload | Claims: `sub`, `exp`, `roles`, custom |
| Signature | HMAC or asymmetric sign over header+payload |

**Not encrypted** by default—anyone can read payload; do not put secrets inside. **Integrity** from signature when secret/public key verified.

Spring resource server (conceptual):

```java
http.oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()));
```

Validates signature, `exp`, issuer (`iss`), audience (`aud`).

### Trade-offs

| JWT access token | Session cookie |
|------------------|----------------|
| Horizontally scalable validation | Instant server revoke |
| Needs short TTL + refresh strategy | Heavier server state |

**Refresh tokens:** long-lived, stored securely, rotated on use; detect reuse to revoke family.

## 6. OAuth 2.0 and OIDC (overview)

| Role | Entity |
|------|--------|
| Resource owner | User |
| Client | Your app |
| Authorization server | Issues tokens (IdP) |
| Resource server | API validating tokens |

**Authorization Code + PKCE** — standard for public clients (SPAs, mobile): no client secret in browser; PKCE prevents interception.

**OIDC** adds `id_token` (JWT) with identity claims (`email`, `name`).

## 7. Edge Cases

- **Clock skew** — validate `exp` with small leeway.
- **`none` algorithm** attacks — reject; use allow-listed algorithms.
- **Logout** — JWT until expiry unless blocklist or short TTL.

## 8. Common Mistakes

### Incorrect — JWT in `localStorage` without XSS hardening

Prefer HttpOnly cookie patterns or strict CSP when possible.

### Incorrect — embedding permissions that never refresh until token expires

Use short access token + server-side permission checks for sensitive actions.

## 9. Revision and Mastery

- Why is JWT not a substitute for HTTPS?
- When to choose session cookies over JWT for a same-site web app?

## 10. Exercises

1. List claims you would require in an access token for a billing API.
2. Draw OAuth code flow with PKCE for a mobile app.
3. Compare revoke strategies for session vs JWT.

## 11. Chapter Summary

Sessions keep state server-side; JWTs carry signed claims for stateless validation; OAuth/OIDC standardize delegated login. Spring Security integrates each mode via filter configuration—policies remain your responsibility.

## 12. Next Steps

[03-spring-security-patterns.md](03-spring-security-patterns.md)

## 5. Mechanism and Execution Flow *(mandatory)*

### Session cookie path

Login validates password → server stores session → `Set-Cookie: SESSION=...; HttpOnly; Secure; SameSite=Lax` → subsequent requests include cookie → session store lookup.

### JWT bearer path

Client sends `Authorization: Bearer <jwt>` → resource server validates signature, `exp`, `iss`, `aud` → builds `Authentication` without DB hit (unless introspection).

OAuth code flow (Illustrative): browser redirects to IdP → authorization code → backend exchanges code + PKCE verifier for tokens—never store IdP password in your DB.

## 21. FAQ — Sessions and tokens *(mandatory depth)*

**Q: Encrypt JWT payload?** Use JWE if confidentiality required; default JWT is signed, not encrypted.

**Q: Refresh token storage?** Server-side rotation table or secure HttpOnly cookie—not localStorage if XSS risk.

**Q: Same-site cookies?** `SameSite` reduces CSRF; cross-site SPAs may need BFF pattern.

**Q: Logout with JWT?** Short TTL + denylist for refresh tokens until expiry.

**Q: OAuth scopes vs app roles?** Map scopes to authorities in resource server.


## Depth appendix — progressive teaching (Authentication / authorization)

### Incorrect vs corrected patterns

Documenting failure modes accelerates review. Each pair below states **why** the wrong approach breaks client or operator expectations.

### Operational checklist before marking API complete

- OpenAPI matches deployed routes (Ch.03).
- Error envelope consistent (ProblemDetail or documented custom schema).
- AuthN/Z enforced on mutations (Module 39).
- Examples in `examples/` compiled on Temurin 21.0.12.1 with recorded output.
- Versioning/deprecation policy published for partners.

### Revision drills (Track B)

1. Explain one design trade-off you reversed after load testing.
2. Write three MockMvc or plain HTTP negative tests not in the happy path.
3. Identify one boundary where DTO mapping must never leak entities.

### References (official hierarchy)

- RFC 9110 — HTTP semantics (methods, status).
- Jakarta EE / Spring reference docs for Illustrative framework binding only.
- OWASP ASVS — verification levels for security chapters (Module 43).

**Mechanism merge note:** Where earlier sections already diagram execution flow, this appendix reinforces **operational** depth without duplicating definitions.

### Threat modeling prompts

- Credential stuffing → rate limit + MFA policy.
- Session fixation → rotate session id after login.
- Token theft → short TTL, refresh rotation, detect reuse.

### Audit fields

Store `createdBy` / `modifiedBy` from principal on mutations; support incident investigation (Module 47).

### Multi-tenant isolation

Every repository query includes `tenant_id` predicate derived from token—not from request body parameters.

## Technical L3 supplement — teaching depth

### Motivation recap (concrete scenario)

A team ships a Java 21 service without documented HTTP contracts, security rules, or messaging guarantees. Mobile clients cache error shapes; a deploy renames a field and checkout silently fails. Operations cannot trace requests across services. The material in this module prevents that class of failure by making **boundaries explicit**: REST semantics, DTOs, auth filters, architecture ports, patterns, clean code, security controls, resilience, and delivery semantics.

### Mechanism trace (how this chapter shows up at runtime)

1. **Ingress** — HTTP or message enters an adapter (controller, listener).
2. **Policy** — auth, validation, and rate limits run before domain logic (fail closed).
3. **Application** — use case coordinates domain + ports inside a transaction or saga step.
4. **Egress** — DTO mapping, event publish (outbox), or outbound HttpClient with timeouts.
5. **Observation** — structured logs with correlation id; metrics on error rate (Module 47).

This sequence is framework-agnostic; Spring/Boot annotations are **Illustrative** wiring for the same pipeline taught in earlier modules.

### Progressive example ladder

| Stage | Focus | Verification label |
|-------|--------|-------------------|
| Minimal | JDK-only demo or pure unit logic | Verified / Compile-verified |
| Realistic | Layered service with DTO + port | Illustrative Spring config |
| Advanced | Failure modes: timeout, duplicate message, 409 conflict | Test + documented output |

Never label **Verified** without Temurin 21.0.12.1 compile/run recorded in chapter or `PROGRESS.md`.

### Wrong vs right (review board)

| Wrong | Why it hurts | Right |
|-------|----------------|-------|
| Skip server AuthZ | IDOR incidents | Check principal vs resource every mutation |
| Expose JPA graph in JSON | Lazy-load storms, schema coupling | Map to DTO in service |
| Infinite HttpClient wait | Thread pile-up, incident duration | Connect + request timeouts |
| Retry all POSTs | Duplicate charges | Idempotency keys + dedup store |
| Dual write DB + broker | Lost or phantom events | Transactional outbox |

### Design tensions engineers negotiate

- **Consistency vs availability** during partitions (Module 44).
- **Stateless JWT vs revocable sessions** (Module 39).
- **Modular monolith vs microservices** operational cost (Module 40).
- **Kafka vs Rabbit** retention and routing (Module 45).
- **Strict SOLID vs delivery speed** (Modules 41–42).

Document the choice in ADRs; the handbook gives options, not one universal answer.

### Java 21 notes

- **Records** for immutable DTOs and events (`Module 08`, `15`).
- **Virtual threads** improve blocking HTTP and JDBC throughput but do not remove timeouts or auth checks.
- **Pattern matching** and **sealed types** clarify domain result models (`PaymentResult` in SOLID chapters).

### Compatibility and sources

Prefer **RFC 9110** for HTTP, **RFC 7807** for errors, **OWASP ASVS** for verification language, and vendor docs for brokers labeled Illustrative. Distinguish **JLS/API guarantees** from **Spring/HotSpot** behavior as in `DOCUMENTATION-STANDARDS.md`.

### Track B — mastery questions

1. Where is the transaction boundary in your current project, and why?
2. Which dependency would you invert first behind a port for testing?
3. What status code and error shape would clients see for your most common validation failure?
4. How would you prove an idempotent consumer in tests?
5. What metric would fire before users notice a downstream slowdown?

### Track B — field exercise (no solution in chapter)

Implement or extend one `examples/` demo for this module: add a negative-path assertion (4xx, breaker OPEN, or rejected input), compile with `javac -source 21`, run on Temurin 21.0.12.1, paste output into `PROGRESS.md` only after a real run.

### Cross-module links

| Need | Module |
|------|--------|
| HTTP client timeouts | 32 |
| JDBC transactions | 33–35 |
| Spring DI / Security | 36–37 |
| Testing & CI | 30 |
| Observability | 47 |

### Glossary touchpoints

Add unfamiliar terms encountered here to `GLOSSARY.md` when stabilizing definitions across the handbook (repository workflow in java-documentation skill).

### Explicit omissions (this supplement)

- Vendor-specific console screenshots and cloud provisioning scripts.
- Exploit reproduction steps (defensive security only in Module 43).
- Exhaustive broker tuning guides (operational manuals remain external).

## Token lifecycle operations *(conditional)*

- Document access token TTL and refresh rotation policy.
- Runbook for key rotation (JWKS publish overlap).
- Metrics on 401 rate vs login success rate.

## Review Checklist

- [x] EXPAND-PRESERVE 2026-11-11 (Technical L3)
- [x] Session vs JWT; OAuth roles; security pitfalls
