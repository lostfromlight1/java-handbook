# Example: h2-jdbc-demo

Embedded H2 in-memory JDBC (Maven). Also demonstrates JUnit 5 test execution.

```bash
cd examples/backend/h2-jdbc-demo
mvn -q -DskipTests compile exec:java
mvn -q test
```

Expected exec: `name=Ada`, `rowCount=1`, `jdbcH2=ok`.

Expected test: BUILD SUCCESS with tests run.

Requires Apache Maven 3.9+ and network for first dependency download.

**Without Maven:** download `h2-2.3.232.jar` to `lib/`, then:

```powershell
javac -cp lib\h2-2.3.232.jar -d out src\main\java\com\handbook\jdbc\JdbcH2Demo.java
java -cp "out;lib\h2-2.3.232.jar" com.handbook.jdbc.JdbcH2Demo
```
