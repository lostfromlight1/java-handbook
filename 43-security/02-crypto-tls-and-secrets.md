# Crypto, TLS, and Secrets

> **Status:** Completed  
> **Module:** 43 — Security  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10

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

## Review Checklist

- [x] Defensive crypto guidance; no attack procedures
