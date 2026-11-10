# Examples

Runnable demos for handbook chapters. Prefer compiling from each demo’s own directory with the commands in that folder’s `README.md`.

## IDE note (Cursor / VS Code Java)

Most demos are **standalone `javac` programs** (often in the default package). They are **not** one multi-module Maven/Gradle app.

If the Java language server imports them as a single project, you will see false errors such as:

- `associated to the named module 'com.handbook.greeting'` (from the JPMS demo’s `module-info.java`)
- `The type X is already defined` (duplicate demo helper types like `Person` / `User`)
- JDK APIs “not accessible” (`java.net.http`, `java.lang.management`) under that mistaken modular classpath

Workspace settings in `.vscode/settings.json` **exclude** the CLI demo trees from Java project import. Maven demos under `examples/backend/` stay importable.

After pulling these settings, run **Java: Clean Java Language Server Workspace** and reload the window.

To edit the JPMS demo with full IDE support, open `examples/build-tools/jpms-hello-demo/` as its own workspace folder.

## Layout

| Folder | Focus |
|--------|--------|
| `beginner/` | First programs, `main`, args, simple algorithms |
| `core-java/` | Language and standard library demos |
| `jvm/` | Classpath, memory, and runtime-facing demos |
| `debugging/` | Reproduce-and-fix style samples |
| `networking/` | JDK HTTP and sockets |
| `testing/` | JUnit-oriented samples |
| `build-tools/` | Maven awareness, classpath JARs, JPMS hello |
| `backend/` | JDBC/JPA (Maven), DI/proxy/REST teaching demos, `spring-boot-minimal` (Maven Wrapper) |
| `patterns/` | Creational and structural/behavioral sketches |
| `clean-code/` | Readability and naming demos |
| `security/` | Input-guard / defensive coding demos |
| `resilience/` | Circuit-breaker style sketches |
| `caching/` | In-memory LRU cache |
| `observability/` | Structured logging sketches |
| `advanced/` | Reflection, method handles, FFM preview, Java agents |

Each leaf demo has its own `README.md` with `javac`/`java` commands and verified Temurin 21 output markers.
