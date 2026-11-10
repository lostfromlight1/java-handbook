# Solutions: Module 42 — Clean Code and SOLID

1. Name states intent (`calculateTotal`) so readers search and review without decoding abbreviations.
2. Early returns reduce nesting; failures visible at top.
3. `InvoiceCalculator` changes for tax law; `PdfRenderer` for layout—separate classes.
4. New `PricingStrategy` implementation; inject into `Checkout`.
5. Subtype throws where supertype allows, or narrows preconditions unexpectedly.
6. Split into `OrderQueries` and `OrderCommands` (or role-specific ports).
7. Port interfaces in application/domain; adapters in infrastructure.
8. One class imports HTTP, JDBC, email; thousands of lines; merge conflict hell.
9. Anemic: setter changes status outside rules; rich: `order.cancel()` enforces invariants.
10. Replace with named constant or enum (`OrderStatus.SHIPPED`).

11. `readability=ok`
12. `Money` centralizes scale/rounding rules; raw `BigDecimal` scatters currency bugs.
