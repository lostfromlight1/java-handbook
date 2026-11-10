# Example: jpms-hello-demo

Minimal JPMS two-module application (JDK only).

```bash
cd examples/build-tools/jpms-hello-demo
javac -d mods/com.handbook.greeting com.handbook.greeting/module-info.java com.handbook.greeting/com/handbook/greeting/Greeting.java
javac --module-path mods -d mods/app app/module-info.java app/com/handbook/app/Main.java
java --module-path mods -m app/com.handbook.app.Main
```

Expected: `greeting=jpms-ok`, `module=app`.
