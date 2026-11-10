# Solutions: Module 03 — Control Flow Assessment

## Part A

1. Java has no truthy ints/objects in `if`; condition type is `boolean`.
2. Fall-through continues into the next case without `break`. Arrow labels do not fall through.
3. When the end condition isn’t a simple count (wait until flag/sentinel).
4. `break` exits the loop; `continue` skips to the next iteration.
5. Binds a typed variable only when the test succeeds—less cast boilerplate/errors.
6. Compiler can enforce exhaustiveness over known subtypes.

## Part B

7. B  
8. weekend  
9. `1 3`  
10. `42` (bound `i`)

## Part C

11. Empty statement after `if` due to semicolon; brace block is separate.
12. Add `break`s, or rewrite as switch expression / arrow cases.
13. Use an `Iterator.remove` or collect-then-remove; don’t structurally modify during enhanced for.

## Part D

14. Standard FizzBuzz loop 1..15.  
15. Example:

```java
return switch (code) {
    case 200 -> "ok";
    case 404 -> "not-found";
    case 500 -> "error";
    default -> "other";
};
```

16. Mirror `PatternControlDemo.describe`.

## Part E

17. Similar to `Shape` in PatternControlDemo.  
18. A general `case Object o` before specific cases can make later cases unreachable.
