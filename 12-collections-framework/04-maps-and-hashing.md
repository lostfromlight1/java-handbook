# Maps and Hashing

> **Status:** Completed  
> **Module:** 12 — Collections Framework  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-11  
> **Chapter profile:** Technical
> **Depth target:** Level 3 (`HashMap`, buckets, `equals`/`hashCode` contract)
> **Examples verified with:** Temurin JDK 21.0.12.1 (`javac 21.0.12.1`)

**Section policy:** Sections are **conditional unless marked mandatory**. Include a section when it adds meaningful learning value. Do **not** manufacture content to fill headings. For omitted sections, record the reason in the Review Checklist.

| Mandatory for most chapters | Conditional |
| --------------------------- | ----------- |
| Learning objectives | Internal behavior |
| Prerequisites | Performance |
| Why this exists | Security |
| Conceptual explanation | Language comparisons |
| Mechanism and execution flow *(or merge note)* | Advanced examples |
| Terminology | Real-world use cases |
| Examples | Design trade-offs *(strongly encouraged for Technical / Deep-dive)* |
| Common mistakes | Compatibility details |
| Summary and references | — |

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Use **`Map`** operations: **`put`**, **`get`**, **`getOrDefault`**, **`containsKey`**, **`remove`**, **`size`**.
- Explain how **`HashMap`** locates entries using **`hashCode`** then **`equals`** on keys.
- Connect map behavior to the **`equals` contract** and **`hashCode` contract** from **Module 06 Ch.04**.
- Implement or recognize **correct key types** for hash maps (immutable fields in `equals`/`hashCode`).
- Preview **`LinkedHashMap`** and **`TreeMap`** ordering differences.

## 2. Prerequisites

- **Module 06 Ch.04** — [`equals-hashcode-and-tostring.md`](../06-classes-and-objects/04-equals-hashcode-and-tostring.md): equal objects **must** share `hashCode`; override both together for value types.
- **Module 12 Ch.03** — hash sets as hash tables; `TreeSet` comparison order.
- **Demo reference:** [`EqualsHashCodeDemo.java`](../../examples/core-java/equals-hashcode-demo/EqualsHashCodeDemo.java).

Brief refresher: **`hashCode`** narrows search to a **bucket**; **`equals`** confirms the right key among possible collisions in that bucket. Breaking the contract—two equal keys with different hash codes—can store **duplicate logical keys** or fail **`get`**.

## 3. Why This Concept Exists

Lookup tables are everywhere: **user id → profile**, **SKU → stock**, **locale code → message template**. A **`List`** finds by index, not by arbitrary key. **`Map<K,V>`** models **associative** storage: given a key, retrieve the paired value in expected constant time for hash maps.

Without disciplined **`equals`/`hashCode`** on keys, maps silently misbehave—the classic production bug after refactoring a key class.

## 4. Conceptual Explanation

### 4.1 Map is not a Collection of entries you iterate like a List

**`Map<K,V>`** stores **entries** (key–value pairs). Views:

- **`keySet()`** — `Set<K>`
- **`values()`** — `Collection<V>`
- **`entrySet()`** — `Set<Map.Entry<K,V>>`

You **`put`** by key; duplicate **`put`** with equal key **replaces** the value (for standard `HashMap`).

### 4.2 HashMap algorithm (conceptual)

1. Compute **`hash = key.hashCode()`** (spread/stripped by map implementation).
2. Pick **bucket index** from table length.
3. Walk bucket chain/tree; compare keys with **`equals`**.
4. On hit: return or replace value; on miss: insert new node.

```
bucket[i]:  (key A, value 1) -> (key A', value 2)   // collision chain if hash matches bucket
```

OpenJDK 8+ **treeifies** long chains in a bucket to keep worst case manageable when hashes collide.

### 4.3 The M06 connection — contracts in practice

From Module 06:

- If `a.equals(b)`, then `a.hashCode() == b.hashCode()`.
- If you override **`equals`**, override **`hashCode`** with the **same fields**.
- Mutating fields used in `hashCode` after insert **orphans** the entry.

**`MapsHashingDemo`** uses a **`Key`** type:

```java
@Override
public boolean equals(Object o) {
    return o instanceof Key k && Objects.equals(id, k.id);
}

@Override
public int hashCode() {
    return Objects.hashCode(id);
}
```

Two distinct `new Key("A")` objects are **equal**; map size stays **`1`**; **`get(new Key("A"))`** returns **`"two"`** (second put replaced value).

If **`hashCode`** were constant `0` but `equals` worked, the map still **functions** but degenerates to one bucket—**O(n)** operations.

If **`equals`** were identity (`==`) but logical ids match, **`get`** would miss—**size could be 2** for “same” id.

### 4.4 null keys and values

**`HashMap`** allows **one** `null` key and multiple `null` values. **`TreeMap`** forbids `null` keys (natural ordering cannot compare null). **`Map.of`** forbids null keys and values.

### 4.5 LinkedHashMap and TreeMap

- **`LinkedHashMap`** — hash performance with **insertion-ordered** or **access-ordered** iteration (LRU caches use access order with `removeEldestEntry` override).
- **`TreeMap`** — sorted by key **`Comparable`** or **`Comparator`** — **`O(log n)`** operations.

## 5. Mechanism and Execution Flow

### 5.1 `HashMap.get(key)`

1. If key is `null`, use **null bucket** handling (allowed in `HashMap`).
2. Otherwise **`hash(key.hashCode())`** selects bucket index (implementation uses spread bits).
3. Walk bucket (**list or tree** after treeify) comparing **`equals`** on keys.
4. Return associated **value** or `null` (ambiguous with absent key vs null value—use `containsKey`).

### 5.2 `put` and replacement

1. Find bucket like **`get`**.
2. If matching key exists, **replace value** and return old value.
3. Else insert entry; increment **`size`**; maybe **resize** when load factor exceeded (Ch.05).

### 5.3 Key discipline (`MapsHashingDemo`)

Mutable keys that change **`hashCode`** after insert **break the map**—store by **stable id** (`String`, `Integer`, record with value-based `hashCode`).

## 6. Terminology

| Term | Meaning |
|------|---------|
| **Key** | Lookup identifier; uniqueness by `equals` in `HashMap` |
| **Value** | Payload associated with a key |
| **Entry** | `Map.Entry` pairing key and value |
| **Bucket** | Slot in hash table array holding zero or more entries |
| **Collision** | Two keys mapping to same bucket index |
| **Load factor** | Threshold (default 0.75) triggering resize/rehash |
| **Rehash** | Rebuild table with larger capacity when load factor exceeded |

## 7. Syntax and Language Rules

```java
import java.util.HashMap;
import java.util.Map;

Map<String, Integer> ages = new HashMap<>();
ages.put("Ada", 36);
ages.put("Ada", 37);              // replaces value
int n = ages.getOrDefault("Lin", 0);

for (Map.Entry<String, Integer> e : ages.entrySet()) {
    System.out.println(e.getKey() + "=" + e.getValue());
}
```

- **`Map.of("a", 1, "b", 2)`** — small immutable maps (Java 9+); no nulls; duplicate key → `IllegalArgumentException` at creation.
- **`putIfAbsent`**, **`merge`**, **`compute`** — Java 8+ functional-style updates.

## 8. Step-by-Step Examples

### 8.1 Minimal — replace by key

```java
Map<String, String> m = new HashMap<>();
m.put("k", "v1");
m.put("k", "v2");
System.out.println(m.get("k"));  // v2
System.out.println(m.size());      // 1
```

### 8.2 Realistic — MapsHashingDemo

Full source: [`MapsHashingDemo.java`](../../examples/core-java/maps-hashing-demo/MapsHashingDemo.java).

```bash
javac MapsHashingDemo.java && java MapsHashingDemo
```

**Verified output (Temurin 21.0.12.1):**

```text
Ada=37
size=2
containsLin=true
keyedSize=1
keyedA=two
```

**Important lines:**

- Second **`put("Ada", 37)`** overwrites age — not two `"Ada"` keys.
- **`keyed.put(new Key("A"), "one")`** then equal key with **`"two"`** — **`keyedSize=1`**.
- **`get(new Key("A"))`** finds via **hash + equals**, not reference identity.

### 8.3 Advanced — wrong key breaks the map

```java
// Broken — equals uses id but hashCode not overridden
class BadKey {
    final String id;
    BadKey(String id) { this.id = id; }
    @Override public boolean equals(Object o) {
        return o instanceof BadKey k && Objects.equals(id, k.id);
    }
    // hashCode() defaults to identity — equal logical keys may coexist
}
```

**Corrected:** use `Objects.hashCode(id)` or `Objects.hash(id)` like **`Key`** in the demo.

## 9. Real-World Use Cases

- **In-memory caches** — `Map<String, byte[]>` with eviction policy on `LinkedHashMap`.
- **DTO indexing** — build `Map<Long, Order>` once, O(1) lookup in report generation.
- **Configuration maps** — immutable `Map.of` for static label tables.
- **Multi-index** — same data keyed by id vs slug via two maps or one map of composite values.

## 10. Internal Behavior

OpenJDK **`HashMap`** array of buckets; nodes hold hash, key, value, next. Resize doubles capacity when `size > capacity * loadFactor`.

**`String`** keys use optimized `hashCode` cached in the string object—why strings are common map keys.

HotSpot-specific: treeification threshold (8) and untreeify (6) on bucket length.

## 11. Edge Cases and Limitations

- **Not thread-safe** — concurrent `put`/`get` without synchronization can corrupt table (use `ConcurrentHashMap` later).
- **Weak/soft references** — `WeakHashMap` for ancillary caches keyed by objects identity lifecycle.
- **Enum keys** — `EnumMap` compact array indexed by ordinal.
- **Floating-point keys** — possible but fragile due to `==` vs rounding; prefer `Integer` or `String`.

## 12. Common Mistakes

### Incorrect — mutable key

```java
class MutableId {
    String id;
    // equals/hashCode on id
}
MutableId k = new MutableId();
k.id = "A";
map.put(k, 1);
k.id = "B";   // lost entry — hash bucket no longer matches
```

### Corrected

```java
final class UserId {
    private final String id;
    // constructor assigns id; equals/hashCode on id only
}
```

### Incorrect — only overriding equals

Symptom: **`Set`** duplicates and **`Map.get`** misses despite “equal” keys—see Module 06 exercises.

### Corrected

Override **`hashCode`** with same fields; run tests with distinct `new Key(...)` instances.

## 13. Best Practices

- Prefer **`String`**, **`Integer`**, **`Long`**, **enums**, or **immutable value types** as keys.
- Use **`Map.copyOf`**, **`Map.of`**, or unmodifiable wrappers when exposing read-only config.
- For counting: **`merge(key, 1, Integer::sum)`** instead of manual get-null-put.
- Document whether `null` keys/values are allowed in your API.

## 14. Design Trade-offs

| Map type | Lookup avg | Iteration order | null key |
|----------|------------|-----------------|----------|
| `HashMap` | O(1) | Unspecified | One allowed |
| `LinkedHashMap` | O(1) | Insertion/access | One allowed |
| `TreeMap` | O(log n) | Sorted keys | Not allowed |
| `EnumMap` | O(1) | Enum declaration order | Not allowed |

## 15. Performance Considerations

Initial capacity `new HashMap<>(expectedSize, loadFactor)` avoids rehashes. Poor `hashCode` distribution → long buckets → treeify overhead.

## 16. Security Considerations

Do not use attacker-controlled key types with weak `hashCode`. Validate map size when deserializing untrusted JSON objects.

## 17. Java Version and Compatibility Notes

**`HashMap`** since 1.2; linked/tree bucket behavior since Java 8. **`Map.of`** since 9. **SequencedMap** (21) adds `firstEntry`/`lastEntry` on ordered maps.

## 18. Related Language Comparisons

JavaScript object literals are like maps with string/symbol keys; Java separates **`Map`** interface from language syntax for clearer contracts and generics.

## 19. Revision and Mastery

- **Gaps:** forgetting replace semantics of `put`; using mutable POJOs as keys.
- **You should answer:** Why `keyedSize=1` after two puts with different `Key` instances?
- **Advanced:** `IdentityHashMap` uses `==` not `equals`.

## 20. Exercises

### Beginner

1. Build map name→age; overwrite one name; print `size()` and `get`.
2. Predict `keyedA=` in `MapsHashingDemo`.

### Intermediate

3. Count word frequency in a `String` with `HashMap` and `merge`.
4. List steps `HashMap.get` performs for a key.

### Advanced

5. When would `NavigableMap.subMap` beat scanning all keys?

## 21. Interview Questions

1. What happens if two equal keys disagree on `hashCode`?
2. `HashMap` vs `Hashtable`?
3. Can values be null in `HashMap`?

## 22. Solutions

1. Size 1 for one key; get returns latest value.
2. `two`.
3. Loop tokens; `merge(token, 1, Integer::sum)`.
4. `hashCode` → bucket index → `equals` chain.
5. Range queries on sorted keys in `TreeMap`.

## 23. Chapter Summary

**`Map`** associates **keys** with **values**. **`HashMap`** uses **`hashCode`** to choose a bucket and **`equals`** to find the exact key—the same contracts taught in **Module 06 Ch.04**. Override **`equals`/`hashCode`** together for key types; use immutable key fields when possible. **`MapsHashingDemo`** shows overwrite by string key and correct value lookup for equal `Key` objects.

## 24. Key Terminology

`HashMap` · bucket · collision · load factor · `Map.Entry` · key contract

## 25. References and Further Reading

- [Java SE `Map`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Map.html)
- [Java SE `HashMap`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/HashMap.html)
- Module 06 Ch.04 — equals/hashCode contracts
- [Effective Java, 3rd ed.](https://www.oracle.com/java/technologies/effective-java.html) Items on `equals` and `hashCode`

## 26. Next Steps

- [05-internals-and-choosing-structures.md](05-internals-and-choosing-structures.md) — pick structures and safe iteration
- Module 13 — `Map<String, List<T>>` generic nesting

## Review Checklist

**Mandatory Depth Review:** Technical profile; hash map lookup and key equality at **Level 3**.

- [x] Learning objectives covered
- [x] Prerequisites identified and briefly refreshed
- [x] Chapter profile and depth target recorded and appropriate
- [x] Depth gate passed (detailed definitions; progressive examples)
- [x] Mandatory Depth Review (Levels 1–4) passed for this topic
- [x] Mechanism and execution flow explained where applicable (§5)
- [x] Complex relationships illustrated where useful
- [x] First-appearance concepts explained now (not deferred away)
- [x] Terminology defined before use
- [x] Examples include sufficient context to compile and run (or labeled Illustrative)
- [x] Verification status accurately labeled (**Verified** on Temurin JDK 21.0.12.1 where demos exist)
- [x] Explanations distinguish specification guarantees from implementation details where relevant
- [x] References support important technical claims
- [x] Solutions explain reasoning, not just final answers
- [x] No artificial sections, filler, or unexplained jargon
- [x] Chapter can be understood without repeatedly consulting other chapters
- [x] Conditional omissions recorded below
- [x] Revision and Mastery included when profile requires
- [x] `PROGRESS.md` updated honestly (standards-alignment pass 2026-11-11; Temurin re-verify)

**Omissions (section → reason):**

- **Treeify threshold / split policy details** — sketched in §10; Ch.05 ties to resize.

**Verification note:** `MapsHashingDemo` (`Ada=37`; `size=2`; `containsLin=true`) on Temurin JDK 21.0.12.1 (2026-11-11 re-run).
