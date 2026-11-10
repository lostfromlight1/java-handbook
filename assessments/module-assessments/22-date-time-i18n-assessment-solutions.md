# Solutions: Module 22

1. **`Period`** — date-based (years/months/days); **`Duration`** — time-based (hours/minutes/nanos).
2. **No zone context** — ambiguous globally; use `Instant` or `ZonedDateTime`.
3. **UTC** timeline (not local civil time).
4. Converts to another zone keeping the **same instant** (different local fields).
5. **`java.util.Date` / `Calendar`**.
6. **`messages.properties`** (default bundle) or `messages_en.properties` depending on chain.
7. **Orthogonal** — locale is formatting/language; zone is offset rules.
8. **Yes** — immutability implies thread-safe.
9. **22:00** (UTC+6:30).
10. Avoid **console encoding** issues with currency symbols; still show EUR via code.

11. `36`
12. `22:00`
13. `Hello`

14. **`Instant` + `ZoneId`** (or offset) for faithful replay.
15. **No** — use `NumberFormat` / locale from user context.
