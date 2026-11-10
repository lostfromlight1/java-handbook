# Example: classpath-jars-demo

Builds a JAR on the classpath from multiple packages.

```bash
cd examples/build-tools/classpath-jars-demo
javac -d out app/App.java util/Greeter.java
jar --create --file=lib/greeter-app.jar -C out .
java -cp lib/greeter-app.jar app.App Handbook
```

Expected keys: `greet=Hello, Handbook!`, `fromJar=true`.

Do not commit `out/`, `lib/*.jar`, or `.class` files.
