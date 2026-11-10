# Locale and Internationalization

> **Status:** Completed  
> **Module:** 22 — Date, Time, and Internationalization  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical  
> **Depth target:** Levels 2–3 (Locale, NumberFormat, ResourceBundle)  
> **Examples verified with:** Temurin JDK 21.0.12.1 (javac 21.0.12.1)

## 1. Learning Objectives *(mandatory)*

- Construct **`Locale`** instances and select formatting rules.
- Format currency and numbers with **`NumberFormat`**.
- Load localized messages from **`ResourceBundle`** property files.

## 2. Prerequisites *(mandatory)*

- Ch.01–02; properties files on classpath.

## 3. Why This Concept Exists *(mandatory)*

**i18n** designs for many markets; **l10n** supplies locale-specific data. Hard-coded English strings and `US` number formats break German or Japanese expectations.

## 4. Conceptual Explanation *(mandatory)*

- **`Locale`** — language, country, variant (`en_US`, `de_DE`).
- **`NumberFormat`** — decimal grouping and currency symbols per locale.
- **`ResourceBundle.getBundle(baseName, locale)`** — resolves `baseName_lang_COUNTRY.properties`.
- **Locale ≠ time zone** — orthogonal; combine with Ch.02 formatters for full datetime i18n.

## 5. Mechanism and Execution Flow *(mandatory)*

1. Create `Locale.US` and `Locale.GERMANY`.
2. `NumberFormat.getCurrencyInstance(locale)` for money display.
3. `ResourceBundle.getBundle("messages", locale)` loads keys; `getString("greeting")`.

Bundle chain: exact locale → language-only → root.

## 8. Step-by-Step Examples *(mandatory)*

Demo: [locale-i18n-demo](../../examples/core-java/locale-i18n-demo/).

**Verified output (Temurin JDK 21.0.12.1):**

```text
usPrice=$1,999.50
deNumber=1.999,5
deCurrency=EUR
usdCode=USD
greeting=Hello
farewell=Goodbye
```

## 11. Edge Cases and Limitations *(strongly encouraged)*

- Console encoding may garble symbols—demo documents `deCurrency=EUR` fallback.
- Missing bundle keys → `MissingResourceException`.
- `MessageFormat` plural rules deferred to framework modules.

## 12. Common Mistakes *(mandatory)*

Hard-coded user-visible strings in services instead of bundle keys.

## 16. Security Considerations *(conditional)*

Bundles are not cryptographic secrets—do not store credentials in properties.

## 23. Chapter Summary *(mandatory)*

**Locale** selects formatting; **ResourceBundle** selects text—pair with `java.time` formatters for complete user-facing datetime and messaging.

## 26. Next Steps

Module **23** — Regular Expressions.

## Review Checklist

- [x] Technical; Verified Temurin 21.0.12.1
- [x] Encoding note preserved
