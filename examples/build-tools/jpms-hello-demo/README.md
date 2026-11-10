# Example: jpms-hello-demo

Minimal JPMS two-module application (JDK only).

**Important:** this folder contains `module-info.java`. If Cursor/VS Code indexes it as part of the whole handbook workspace, other demos light up with false “named module `com.handbook.greeting`” errors. The workspace excludes this tree from Java import (see `.vscode/settings.json` and [../../README.md](../../README.md)). Open **this folder alone** if you want IDE module support.

```bash
cd examples/build-tools/jpms-hello-demo
javac -d mods/com.handbook.greeting com.handbook.greeting/module-info.java com.handbook.greeting/com/handbook/greeting/Greeting.java
javac --module-path mods -d mods/app app/module-info.java app/com/handbook/app/Main.java
java --module-path mods -m app/com.handbook.app.Main
```

Expected: `greeting=jpms-ok`, `module=app`.
