# Solutions: Module 23

1. **`\\d`** (regex `\d` in source).
2. **Entire string** must match (implicit anchors).
3. **Yes** — immutable compiled pattern.
4. **No** — one matcher per thread or synchronize.
5. **`(?<name>...)`**.
6. **`find()`** can find substring; **`matches()`** requires whole region match.
7. **Limit input length**, possessive quantifiers, timeout layer, avoid nested greedy backtracking.
8. **First capturing group** substitution.
9. **Nested HTML/XML grammars**, full programming languages—use parsers.
10. **Compilation cost** amortized in hot loops.

11. `1042`
12. `42`
13. `false`

14. **Often no** — use library validation + IDNA rules as needed.
15. **No** — create per-thread matchers via `pattern.matcher(input)`.
