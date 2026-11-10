# Sessions, JWT, and OAuth

> **Status:** Completed  
> **Module:** 39 — Authentication and Authorization  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

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

## Review Checklist

- [x] Session vs JWT; OAuth roles; security pitfalls
