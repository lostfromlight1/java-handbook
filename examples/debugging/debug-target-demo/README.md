# Example: debug-target-demo

Run with a breakpoint on the `if` line in an IDE, or read output for logic-debug practice.

```bash
javac DebugTargetDemo.java && java DebugTargetDemo
```

Expected: `count=1` (buggy: lengths strictly *greater* than 3); `expectedCorrect=1` is the fixed `>=` semantics.
