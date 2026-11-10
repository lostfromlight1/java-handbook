# Installing Java and Environment Setup

> **Status:** Completed  
> **Module:** 01 — Introduction to Java  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Oracle JDK 21.0.9

## 1. Learning Objectives

After completing this chapter, you should be able to:

- Install a JDK 21 from a trusted vendor on your OS family.
- Explain `PATH` and `JAVA_HOME` and verify they point at the intended JDK.
- Confirm success with `java`, `javac`, and a tiny program.
- Troubleshoot common “wrong Java” and “command not found” failures.
- Document your environment for future chapters and bug reports.

## 2. Prerequisites

- [01-jdk-jre-jvm-and-tooling.md](01-jdk-jre-jvm-and-tooling.md)
- Module 00 HelloWorld experience

## 3. Why This Concept Exists

Most early Java pain is environment configuration, not language semantics. Fix the install once, carefully, so every later example is trustworthy.

## 4. Conceptual Explanation

### 4.1 What to install

Install a **JDK 21** (LTS), not a random outdated bundle. Vendors include Oracle JDK, Eclipse Temurin, and others. This handbook’s validation machine uses **Oracle JDK 21** at `C:\Program Files\Java\jdk-21`.

Prefer official vendor download pages. See [Oracle Java Downloads](https://www.oracle.com/java/technologies/downloads/) and the [SE Support Roadmap](https://www.oracle.com/java/technologies/java-se-support-roadmap.html).

### 4.2 PATH

`PATH` is the list of directories the OS searches for executables. If `jdk-21\bin` is on `PATH`, typing `javac` finds the compiler.

**Problem pattern:** an older `javapath` shim or another JDK appears *before* your intended `bin` directory.

### 4.3 JAVA_HOME

`JAVA_HOME` conventionally points at the JDK root (the folder that contains `bin`, not `bin` itself). Many build tools (Maven/Gradle—Module 29) read `JAVA_HOME`.

Verified on the handbook machine:

```text
JAVA_HOME=C:\Program Files\Java\jdk-21
```

### 4.4 Installation checklist

1. Download JDK 21 for your OS/CPU.
2. Install using the vendor installer or unpack an archive.
3. Set `JAVA_HOME` to the JDK root.
4. Put `%JAVA_HOME%\bin` (Windows) or `$JAVA_HOME/bin` (macOS/Linux) early on `PATH`.
5. Open a **new** terminal.
6. Run verification commands below.
7. Compile and run HelloWorld or RuntimeInfo.

### 4.5 Windows / macOS / Linux notes

| OS | Typical tips |
|----|----------------|
| Windows | Installer often sets `PATH`; still verify with `where java`. Use a new PowerShell/CMD after changes. |
| macOS | `/usr/libexec/java_home` helps locate installs; ensure your shell profile exports `JAVA_HOME`. |
| Linux | Package managers or Temurin archives; avoid mixing distro `default-java` blindly with project pins. |

Exact UI clicks change; the verification commands do not.

## 5. Terminology

| Term | Meaning |
|------|---------|
| PATH | Executable search list |
| JAVA_HOME | Environment variable for JDK root |
| Shim / javapath | Helper executable that redirects to a Java install |
| Soft/hard link | OS aliasing that can confuse which binary runs |

## 6. Syntax and Language Rules

Environment variables are OS shell syntax, not Java language syntax.

PowerShell examples:

```powershell
echo $env:JAVA_HOME
java -version
javac -version
where.exe java
```

Bash examples:

```bash
echo "$JAVA_HOME"
java -version
javac -version
which java
```

## 7. Step-by-Step Examples

### 7.1 Minimal — verify versions

**Verified:**

```text
java version "21.0.9" 2025-10-21 LTS
javac 21.0.9
```

### 7.2 Realistic — confirm home and RuntimeInfo

```bash
javac RuntimeInfo.java
java RuntimeInfo
```

**Verified** `java.home = C:\Program Files\Java\jdk-21` on the handbook machine ([RuntimeInfo](../examples/beginner/runtime-info/)).

### 7.3 Advanced — detect duplicates

On Windows, `where.exe java` listed both:

```text
C:\Program Files\Common Files\Oracle\Java\javapath\java.exe
C:\Program Files\Java\jdk-21\bin\java.exe
```

Knowing the order matters: the first hit is what you usually get.

## 8. Real-World Use Cases

- Onboarding scripts that fail if `JAVA_HOME` unset
- CI agents with multiple JDKs (`JAVA_HOME` per job)
- Supporting older production 17 while developing 21 locally—intentional dual installs

## 9. Internal Behavior

The `java` launcher selects a VM and reads settings/properties. Your shell only decides *which* `java` binary starts. IDE launches may ignore your shell `PATH` and use a configured SDK path instead (next chapter).

## 10. Edge Cases and Limitations

- Corporate machines may block installers or require managed runtimes.
- WSL vs Windows JDKs are separate environments.
- Version managers (`sdkman`, etc.) change `PATH` per shell—document which shell you use.

## 11. Common Mistakes

### Incorrect

Setting `JAVA_HOME` to `...\jdk-21\bin`.

### Corrected

`JAVA_HOME` = JDK root; `bin` goes on `PATH`.

### Incorrect

Changing env vars but not opening a new terminal.

### Corrected

Restart the terminal (or log out/in) so it reloads variables.

### Incorrect

Assuming the IDE uses the same JDK as the terminal without checking.

### Corrected

Configure the IDE Project SDK explicitly (Chapter 03).

## 12. Best Practices

- One “learning” JDK 21 first; add others later deliberately.
- Keep notes: vendor, version, `JAVA_HOME`, OS.
- Prefer `--release 21` later in builds so accidental newer JDKs don’t silently raise the bytecode baseline without intent.

## 13. Design Trade-offs

| Approach | Benefit | Cost |
|----------|---------|------|
| System-wide JDK | Simple | Conflicts across projects |
| Per-project toolchain (Maven/Gradle toolchains) | Isolation | More setup (Module 29) |
| Containerized JDK | Reproducible CI | Docker knowledge |

## 14. Performance Considerations

Install location on HDD vs SSD affects compile speed slightly; negligible vs learning goals.

## 15. Security Considerations

Use official downloads; verify checksums when your org requires it. Keep JDKs patched—security updates matter for production.

## 16. Java Version and Compatibility Notes

Primary: **21**. Newer LTS 25 exists; this course stays on 21 unless a chapter says otherwise.

## 17. Related Language Comparisons

Comparable to installing a Go toolchain or .NET SDK: one SDK pin per project avoids “works on my machine” version drift.

## 18. Revision and Mastery

### Gaps to check

- CI vs laptop JDK skew
- Docker base images with unexpected majors
- Build tools ignoring `JAVA_HOME` due to wrapper config

### Questions

1. What should `JAVA_HOME` point to?
2. How do you prove which `java` runs?
3. Why open a new terminal after changing env vars?

## 19. Exercises

### Beginner

1. Write your `java -version`, `javac -version`, and `JAVA_HOME` in a notes file.
2. Re-run HelloWorld from Module 00.

### Intermediate

3. Temporarily put a wrong directory first on `PATH` in a **temporary** shell session and observe the failure; restore it.
4. Compare IDE SDK path (if installed) to terminal `java.home`.

### Advanced

5. Install a second JDK (optional) and switch `JAVA_HOME` between them; run RuntimeInfo each time.
6. Read your vendor’s installation docs and list three install options (MSI, zip, package manager).

## 20. Interview Questions

1. **What is JAVA_HOME?** — JDK root used by tools/scripts.  
2. **How to debug wrong Java version?** — `where`/`which`, print versions, check PATH order, IDE SDK.  
3. **Why pin JDK 21?** — Reproducible builds and known language/API level.  

## 21. Solutions

1–2. Machine-specific; handbook sample values above.  
3. Expect command not found or wrong major; restore PATH.  
4. Note any mismatch.  
5. `java.home` should change with JAVA_HOME when PATH is consistent.  
6. Answers depend on vendor docs you read—cite them.

## 22. Chapter Summary

Install JDK 21, set `JAVA_HOME` to the JDK root, put `bin` on `PATH`, verify with `java`/`javac`, and confirm with a small program. Watch for multiple binaries on `PATH`.

## 23. Key Terminology

PATH · JAVA_HOME · JDK root · verification · shim

## 24. References and Further Reading

- [Oracle Java Downloads](https://www.oracle.com/java/technologies/downloads/)
- [Installation instructions linked from vendor download pages](https://www.oracle.com/java/technologies/downloads/)

## 25. Next Steps

[03-ide-and-command-line-workflow.md](03-ide-and-command-line-workflow.md)

## Review Checklist

- [x] Environment verification recorded from real machine
- [x] Depth gate passed for install/PATH/JAVA_HOME
- [x] Revision and Mastery included
