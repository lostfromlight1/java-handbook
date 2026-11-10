# Bytecode and Execution

> **Status:** Completed  
> **Module:** 26 — JVM Internals  
> **Target Java version:** 21  
> **Last reviewed:** 2026-11-10  
> **Examples verified with:** Temurin JDK 21.0.12.1 (`javap` + run)

## 1. Learning Objectives

- Read **`javap -c`** output for simple methods.
- Relate **operands** (`iload`, `iadd`, `ireturn`) to Java source.
- Separate **class file format** (JVMS) from **disassembly** tool output (`javap`).

## 2. Prerequisites

- Module **01** `javap` intro; Module **04** methods.

## 3. Conceptual Explanation

The JVM is a **stack machine**: most bytecode pops/pushes operand stack slots. Local variables (`iload_1`) hold parameters and locals.

## 4. Step-by-Step Examples

Demo: [bytecode-demo](../../examples/jvm/bytecode-demo/).

**Program output (verified):**

```text
sum=42
```

**`javap -c -private BytecodeDemo` (Temurin 21.0.12.1 — recorded 2026-11-10):**

```text
Compiled from "BytecodeDemo.java"
public class BytecodeDemo {
  public BytecodeDemo();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public int add(int, int);
    Code:
       0: iload_1
       1: iload_2
       2: iadd
       3: ireturn

  public static void main(java.lang.String[]);
    Code:
       0: new           #7                  // class BytecodeDemo
       3: dup
       4: invokespecial #9                  // Method "<init>":()V
       7: astore_1
       8: getstatic     #10                 // Field java/lang/System.out:Ljava/io/PrintStream;
      11: aload_1
      12: bipush        10
      14: bipush        32
      16: invokevirtual #16                 // Method add:(II)I
      19: invokedynamic #20,  0             // InvokeDynamic #0:makeConcatWithConstants:(I)Ljava/lang/String;
      24: invokevirtual #24                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
      27: return
}
```

`add` loads two `int` parameters and returns their sum. `main` constructs `BytecodeDemo`, calls `add(10,32)`, prints via string concat bootstrap (HotSpot).

## 5. Internal Behavior

- **JVMS:** Opcode meanings and verification rules.
- **HotSpot:** Interpreter + JIT may replace hot `add` with optimized machine code (Module 28).

## 6. Chapter Summary

`javap` connects source to JVMS opcodes; always record output from your JDK when documenting.

## 7. Next Steps

[04-runtime-data-areas.md](04-runtime-data-areas.md)

## Review Checklist

- [x] Real `javap` output recorded
