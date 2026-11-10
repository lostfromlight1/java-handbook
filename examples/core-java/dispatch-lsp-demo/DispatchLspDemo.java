/** Module 07 Ch.03 — dynamic dispatch and LSP (good vs broken substitution). */
public class DispatchLspDemo {
    public static void main(String[] args) {
        Shape[] shapes = { new Rectangle(3, 4), new Square(5) };
        for (Shape s : shapes) {
            System.out.println(s.name() + " area=" + s.area());
        }

        // LSP-friendly: Square is a Shape, not a mutable Rectangle with independent width/height
        Shape squareAsShape = new Square(2);
        System.out.println("poly area=" + squareAsShape.area());
    }
}

abstract class Shape {
    abstract String name();
    abstract int area();
}

class Rectangle extends Shape {
    private final int width;
    private final int height;

    Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    String name() {
        return "Rectangle";
    }

    @Override
    int area() {
        return width * height;
    }
}

class Square extends Shape {
    private final int side;

    Square(int side) {
        this.side = side;
    }

    @Override
    String name() {
        return "Square";
    }

    @Override
    int area() {
        return side * side;
    }
}
