# AbstractClassesDemo

Module 07 Ch.02 — abstract class, abstract `body()`, template method `render()` marked `final`.

```bash
javac AbstractClassesDemo.java
java AbstractClassesDemo
```

**Verified on Temurin JDK 21.0.12.1:**

```text
REPORT:Q1
pdf-body
END
REPORT:Q1
csv-body
END
```

Concepts: `abstract class Report`, concrete `PdfReport` / `CsvReport`, non-overridable `render()` orchestrating `header`, `body`, `footer`.
