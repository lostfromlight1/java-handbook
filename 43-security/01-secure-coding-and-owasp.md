# Secure Coding and OWASP

> **Status:** Completed  
> **Module:** 43 — Security  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

## 1. Learning Objectives

- Map **OWASP Top 10** risks to Java backend mitigations (defensive focus).
- Apply **input validation**, **output encoding**, and **least privilege**.
- Integrate with Module 39 AuthN/Z and Module 38 REST errors.
- Run [InputGuardDemo](../../examples/security/input-guard-demo/).

## 2. Prerequisites

- Modules 32–33 — HTTP, JDBC.
- Module 39 — authentication and authorization.
- Module 38 — DTOs and validation.

## 3. Why This Concept Exists

Applications handle **untrusted input** from browsers, mobile clients, and partners. Secure coding embeds controls in **design and daily habits**, not only penetration tests at release.

## 4. OWASP Top 10 (awareness for Java backends)

The [OWASP Top 10](https://owasp.org/www-project-top-ten/) lists prevalent risk categories. This handbook teaches **prevention**, not attack recipes.

| Risk category | Java backend mitigation |
|---------------|-------------------------|
| **Broken access control** | Server-side AuthZ on every mutation; check resource ownership (Module 39 IDOR) |
| **Security misconfiguration** | Harden defaults; disable dev flags in prod; security headers |
| **Injection** | `PreparedStatement`, parameterized JPA, validated inputs |
| **Insecure design** | Threat modeling in design reviews; fail closed |
| **Cryptographic failures** | Strong algorithms, TLS, secret storage (Ch.02) |
| **Vulnerable components** | Dependency scanning (Module 30 CI), timely upgrades |
| **Authentication failures** | MFA policy, rate limits, secure session/JWT handling |
| **Integrity failures** | Signed artifacts, supply chain checks |
| **Logging/monitoring failures** | Audit security events without leaking secrets (Module 47) |
| **SSRF** | Allow-list outbound URLs; no raw user URLs to `HttpClient` |

## 5. Input validation at boundaries

Validate at **trust boundaries**: HTTP controllers, message consumers, file uploads.

```java
private static final Pattern USER_ID = Pattern.compile("^[a-zA-Z0-9_-]{1,64}$");

static String requireUserId(String raw) {
    if (raw == null || !USER_ID.matcher(raw).matches()) {
        throw new IllegalArgumentException("invalid user id");
    }
    return raw;
}
```

Use **Jakarta Bean Validation** (`@NotNull`, `@Size`) on DTOs; still **re-check authorization** in service layer.

### Incorrect — concatenated SQL

```java
stmt.executeQuery("SELECT * FROM users WHERE id='" + id + "'");
```

### Correct — bind parameters

```java
PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
ps.setString(1, requireUserId(id));
```

## 6. Output encoding and responses

- **JSON APIs:** serializers escape strings; avoid building JSON via string concat.
- **Errors:** `ProblemDetail` without stack traces to clients (Module 38).
- **Headers:** `Content-Type` explicit; CSP for HTML if served.

## 7. Access control

- **Default deny** in security filter chain.
- **Horizontal privilege:** user A must not read order B by id guessing—compare `principal` to resource owner.
- **Vertical privilege:** admin endpoints require role **and** optional network policy.

## 8. Secrets and configuration

Never commit API keys; use environment or secret managers (Ch.02). Log **redaction** for tokens.

## 9. Real-World Use Cases

- Multi-tenant SaaS: tenant id from token, enforced in every query.
- Partner webhook: HMAC signature verification before processing body.
- Admin export: rate limit + audit log entry.

## 10. Edge Cases

- **Validation vs encoding:** validation rejects bad input; encoding protects output context (HTML vs JSON differ).
- **Unicode normalization:** usernames and homoglyphs—define policy for internationalized apps.

## 11. Common Mistakes

| Mistake | Consequence |
|---------|-------------|
| AuthZ only in UI | Trivial API bypass |
| Logging full JWT | Credential leak in log aggregators |
| Trusting client-sent role flags | Privilege escalation |

## 12. Best Practices

- **Security requirements** in user stories.
- **Dependency-Check** / GitHub Dependabot in CI.
- Regular **review** of public endpoints and CORS.

## 13. Security Considerations

This chapter is defensive only. Practice fixes on **your own** systems or sanctioned labs.

## 14. Revision and Mastery

- OWASP category for missing server-side ownership check?
- Why is validation at the controller not enough for AuthZ?
- Name two safe JDBC patterns.

## 15. Exercises

1. Add Bean Validation annotations to a registration DTO.
2. List AuthZ checks for `DELETE /orders/{id}`.
3. Run `InputGuardDemo` and extend with max-length rule.

## 16. Chapter Summary

Secure Java backends combine OWASP-aware design, strict access control, parameterized data access, validated inputs, and operational hygiene—aligned with auth modules and CI dependency discipline.

## 17. Next Steps

[02-crypto-tls-and-secrets.md](02-crypto-tls-and-secrets.md)

## Review Checklist

- [x] OWASP mapped to mitigations; no exploit PoCs; demo verified
