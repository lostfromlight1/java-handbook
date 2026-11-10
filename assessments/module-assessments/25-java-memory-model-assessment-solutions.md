# Solutions: Module 25 — Java Memory Model

1. Heap (shared across threads).
2. `==` false for separate objects; `equals` true if same content.
3. If a hb b, then a visible to b (JLS 17.4).
4. Volatile write hb subsequent read of that volatile.
5. Undefined behavior for those racy accesses.
6. After properly constructed object, other threads see final fields after safe publication.
7. Yes — start hb actions in started thread.
8. No — HB edges, not folk cache model.
9. HB edge when reference becomes visible (volatile, lock, confinement, etc.).
10. No — diagnostic only, can change.

11. `42`
12. `99`
