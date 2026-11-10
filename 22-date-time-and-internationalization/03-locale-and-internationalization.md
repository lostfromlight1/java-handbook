# Locale and Internationalization

> **Status:** Completed  
> **Module:** 22 — Date, Time, and Internationalization  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1

## 1. Learning Objectives

- Use **`Locale`** to select formatting rules.
- Format numbers and currency with **`NumberFormat`**.
- Load messages from **`ResourceBundle`** property files.

## 2. Prerequisites

- Ch.01–02; basic properties files.

## 3. Why This Concept Exists

Users expect dates, numbers, and messages in **their language and conventions**. `java.util.Locale` and resource bundles separate **code** from **translatable text**.

## 4. Conceptual Explanation

- **`Locale`** — language, country, variant (`en_US`, `de_DE`).
- **`NumberFormat.getCurrencyInstance(locale)`** — currency symbols and grouping.
- **`ResourceBundle.getBundle(baseName, locale)`** — loads `baseName_locale.properties`.
- **i18n** — internationalization (design for many locales); **l10n** — localization (specific locale data).

## 5. Step-by-Step Examples

Demo: [locale-i18n-demo](../../examples/core-java/locale-i18n-demo/) with `messages.properties`.

**Verified output:**

```text
usPrice=$1,999.50
deNumber=1.999,5
deCurrency=EUR
usdCode=USD
greeting=Hello
farewell=Goodbye
```

US currency vs German number grouping; bundle keys `greeting`/`farewell`.

## 6. Real-World Use Cases

- E-commerce prices per market.
- Error messages keyed for translation teams.
- `MessageFormat` for pluralization (expand in web modules).

## 7. Edge Cases and Limitations

- **Console encoding** may garble currency symbols on Windows—use UTF-8 or separate currency code (`deCurrency=EUR`).
- **Fallback chain** — bundle missing key throws `MissingResourceException`.
- **Locale** not the same as **time zone**—orthogonal concerns.

## 8. Common Mistakes

Hard-coding English strings in business logic instead of bundle keys.

## 9. Best Practices

- Keep bundles on classpath; use UTF-8 property files with native2ascii only when required.
- Test **Germany vs US** number parsing both directions.

## 10. Chapter Summary

Locale drives formatting; ResourceBundle drives text—combine with `java.time` formatters for full i18n.

## 11. Next Steps

Module 23 — regular expressions.

## Review Checklist

- [x] Verified output  
- [x] Encoding note documented
