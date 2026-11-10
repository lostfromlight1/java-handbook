# Example: classpath-jars-demo

Builds a JAR on the classpath from multiple packages (`app`, `util`).

This demo is meant for **command-line** `javac`/`jar`/`java`. If the IDE shows module/`package` errors, see [../README.md](../README.md) — they are usually language-server import issues, not broken sources.

```bash
cd examples/build-tools/classpath-jars-demo
mkdir -p out lib
javac -d out app/App.java util/Greeter.java
jar --create --file=lib/greeter-app.jar -C out .
java -cp lib/greeter-app.jar app.App Handbook
```

Expected keys: `greet=Hello, Handbook!`, `fromJar=true`.

Do not commit `out/`, `lib/*.jar`, or `.class` files.
