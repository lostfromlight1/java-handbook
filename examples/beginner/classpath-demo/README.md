# Example: classpath-demo

Multi-package program: `app.App` + `util.Greeter`.

```bash
javac -d out app/App.java util/Greeter.java
java -cp out app.App Handbook
```

Expected: `Hello, Handbook!`  
Do not commit the `out/` directory.
