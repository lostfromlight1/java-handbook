# Crypto, TLS, and Secrets

> **Status:** Completed  
> **Module:** 43 — Security  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11
> **Chapter profile:** Technical
> **Depth target:** Levels 2–3

**Section policy:** Omissions recorded in Review Checklist.

## 1. Learning Objectives

- Choose **appropriate cryptography** (hashing vs encryption vs signing).
- Configure **TLS** for Java clients and servers at a conceptual level.
- Manage **secrets** in dev vs production without committing credentials.
- Use JDK **java.security** and **javax.crypto** APIs safely.

## 2. Prerequisites

- Module 32 Ch.03 — TLS overview.
- Module 39 — JWT signing awareness.

## 3. Why This Concept Exists

Developers often **misuse crypto**: reversible “encryption” for passwords, home-grown ciphers, or keys in git. Correct primitives and **key management** protect data in transit and at rest.

## 4. Terminology

| Term | Meaning |
|------|---------|
| **Hash (password)** | One-way fingerprint; use slow algorithms + salt |
| **Symmetric encryption** | Same key encrypt/decrypt (AES) |
| **Asymmetric** | Public/private key pair (RSA, ECDSA) |
| **Digital signature** | Proves integrity + authenticity of data |
| **TLS** | Encrypts and authenticates transport (HTTPS) |
| **KDF** | Key derivation (e.g. PBKDF2, scrypt conceptually) |

## 5. Password storage

**Never** store plaintext passwords. Use a **slow password hash**:

```java
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

final class PasswordHasher {
    private static final int ITERATIONS = 210_000; // tune to policy
    private static final int KEY_LENGTH = 256;

    static String hash(char[] password) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        byte[] derived = pbkdf2(password, salt, ITERATIONS);
        return "pbkdf2$" + ITERATIONS + "$" + Base64.getEncoder().encodeToString(salt)
                + "$" + Base64.getEncoder().encodeToString(derived);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, KEY_LENGTH);
        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
    }
}
```

Production: prefer **delegating to identity provider** or vetted libraries; tune iterations per OWASP guidance.

**Incorrect:** `MessageDigest.getInstance("SHA-256")` alone on passwords (too fast, no salt discipline).

## 6. Encryption at rest (basics)

Use **AES-GCM** with random IV per message; keys from HSM/KMS—not hard-coded.

```java
// Conceptual shape — keys from KMS in production
// Cipher.getInstance("AES/GCM/NoPadding");
```

Do not invent **modes** or **IV reuse** with GCM (catastrophic).

## 7. Signing and JWT

JWTs use **signatures** (HMAC or RSA/ECDSA) so receivers verify integrity. Module 39: validate `aud`, `exp`, issuer; use strong keys and rotation.

## 8. TLS in Java

**HttpClient** (Module 32) uses system trust store by default.

Checklist:

- **TLS 1.2+** only in production configs.
- **Certificate validation** enabled—do not disable hostname verification in prod.
- **mTLS** for service-to-service high trust: client cert presented to server.

Spring Boot: `server.ssl.*` properties; terminate TLS at load balancer **or** app—be explicit about trust boundaries.

## 9. Secrets management

| Environment | Pattern |
|-------------|---------|
| Local dev | `.env` gitignored; sample in `application-dev.properties` with placeholders |
| CI | Masked pipeline variables |
| Production | Vault, AWS Secrets Manager, K8s secrets + encryption at rest |

**Rotation:** design apps to reload credentials without full redeploy when possible.

## 10. Randomness

Use `SecureRandom` for tokens, salts, session ids—not `Random` or `UUID` alone for session secrets without entropy review.

## 11. Real-World Use Cases

- PCI-scoped payment service: HSM-backed keys; no PAN in logs.
- Inter-service JWT: asymmetric keys, public key JWKS endpoint.
- Database **TDE** + app-level field encryption for extra-sensitive columns.

## 12. Edge Cases

- **String passwords in `char[]`:** clear sensitive arrays when possible.
- **Classpath trust stores:** corporate MITM proxies need documented trust imports.

## 13. Common Mistakes

- Hard-coded AES key in source.
- ECB mode (patterns leak).
- Same IV for all messages.

## 14. Revision and Mastery

- When hash vs encrypt?
- What does TLS provide that AES-GCM at rest does not?
- Where should JWT signing keys live?

## 15. Exercises

1. Sketch secret flow from Vault to Spring `@Value` refresh.
2. List TLS termination options for Kubernetes ingress.
3. Compare PBKDF2 parameters in OWASP cheat sheet.

## 16. Chapter Summary

Java security engineering uses standard algorithms through JDK APIs, TLS for transport, KMS for keys, and slow password hashing—never custom crypto or secrets in version control.

## 17. Next Steps

[03-security-testing.md](03-security-testing.md)

## 5. Mechanism and Execution Flow *(mandatory)*

| Operation | Primitive | JDK area |
|-----------|-----------|----------|
| Password storage | Slow hash + salt | `SecretKeyFactory` PBKDF2 |
| TLS transport | Certificates + cipher suites | `SSLContext`, `HttpsURLConnection`, HttpClient |
| Data signing | RSA/ECDSA | `Signature` |
| Symmetric data at rest | AES-GCM | `Cipher` |

Never invent algorithms; use reviewed parameters and key rotation.

## 21. FAQ — Crypto hygiene *(mandatory depth)*

**Q: Hash vs encrypt password?** Hash only; encryption is reversible.

**Q: Store AES key?** KMS/HSM—not properties file in git.

**Q: TLS versions?** Disable legacy SSL; Java 21 defaults are modern—verify server config.

**Q: JWT signing key?** Rotate with overlapping JWKS keys.

**Q: Log secrets?** Never; redact authorization headers.


## Depth appendix — progressive teaching (Security)

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

## Review Checklist

- [x] EXPAND-PRESERVE 2026-11-11 (Technical L3)
- [x] Defensive crypto guidance; no attack procedures
