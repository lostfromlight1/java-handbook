# Foreign Function & Memory — confined arena demo

Allocates off-heap memory in a **confined** `Arena`, writes primitive values, and reads them back. No JNI or native libraries required.

```bash
javac --enable-preview --release 21 FfmArenaDemo.java
java --enable-preview FfmArenaDemo
```

On **JDK 21**, the Foreign Function & Memory API (`java.lang.foreign`) is still a **preview** feature (finalized in a later release). The flags above are required on 21.0.12.1 Temurin.

**Verified (Temurin 21.0.12.1):** `sum=63`; `ffmDemo=ok`

### Native library access (awareness)

Calling into `libc` or other native code via `SymbolLookup` / `Linker` requires unrestricted native access on newer JDK releases. On **JDK 21**, confined off-heap allocation as shown runs without extra flags. If you add native calls and see *Restricted method* / native-access warnings, run with:

```bash
java --enable-native-access=ALL-UNNAMED FfmArenaDemo
```

(Use your module name instead of `ALL-UNNAMED` when the demo is modular.)
