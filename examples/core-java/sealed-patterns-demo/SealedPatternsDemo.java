/** Module 15 Ch.03 — sealed types and patterns. */
public class SealedPatternsDemo {
    public static void main(String[] args) {
        Expr e = new Add(new Const(2), new Const(3));
        System.out.println("eval=" + eval(e));
        System.out.println("desc=" + describe(new Const(7)));
        System.out.println("descAdd=" + describe(e));
    }

    static int eval(Expr expr) {
        return switch (expr) {
            case Const c -> c.value();
            case Add a -> eval(a.left()) + eval(a.right());
            case Neg n -> -eval(n.inner());
        };
    }

    static String describe(Expr expr) {
        return switch (expr) {
            case Const c -> "const:" + c.value();
            case Add a -> "add";
            case Neg n -> "neg";
        };
    }
}

sealed interface Expr permits Const, Add, Neg {}

record Const(int value) implements Expr {}

record Add(Expr left, Expr right) implements Expr {}

record Neg(Expr inner) implements Expr {}
