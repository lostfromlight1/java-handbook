# ImmutabilityBuilderDemo

Module 06 Ch.03 — immutable `Money`, `HttpRequest` with static nested builder.

```bash
javac ImmutabilityBuilderDemo.java
java ImmutabilityBuilderDemo
```

**Verified on Temurin JDK 21.0.12.1:**

```text
1250 USD
GET https://example.com/api Accept=application/json
```

Concepts: `final` fields, `plus` returning new instance, fluent builder, `build()` validation.
