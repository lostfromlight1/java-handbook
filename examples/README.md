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
