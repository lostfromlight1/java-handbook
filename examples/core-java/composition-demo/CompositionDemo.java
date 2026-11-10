/**
 * Module 05 Ch.04 — composition, has-a, aggregation-style ownership.
 * javac CompositionDemo.java && java CompositionDemo
 */
public class CompositionDemo {

    public static void main(String[] args) {
        Engine engine = new Engine("I4", 150);
        Car car = new Car("Civic", engine);
        System.out.println(car.describe());
        System.out.println("start -> " + car.start());

        Address home = new Address("12 Baker St", "London");
        Person person = new Person("Sherlock", home);
        System.out.println(person.describe());

        // Same address object shared (aggregation-style): changing city affects person view
        home = new Address("12 Baker St", "Westminster");
        // person still holds the original Address instance from construction
        System.out.println("person still: " + person.describe());

        Order order = new Order("ORD-1");
        order.addLine(new OrderLine("keyboard", 2, 3500));
        order.addLine(new OrderLine("cable", 1, 500));
        System.out.println(order.summary());
    }
}

class Engine {
    private final String type;
    private final int horsepower;

    Engine(String type, int horsepower) {
        this.type = type;
        this.horsepower = horsepower;
    }

    String describe() {
        return type + " " + horsepower + "hp";
    }

    boolean start() {
        return true;
    }
}

class Car {
    private final String model;
    private final Engine engine; // composition: Car has-an Engine

    Car(String model, Engine engine) {
        this.model = model;
        this.engine = engine;
    }

    String describe() {
        return model + " with " + engine.describe();
    }

    boolean start() {
        return engine.start();
    }
}

class Address {
    private final String street;
    private final String city;

    Address(String street, String city) {
        this.street = street;
        this.city = city;
    }

    String format() {
        return street + ", " + city;
    }
}

class Person {
    private final String name;
    private final Address address; // has-an Address

    Person(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    String describe() {
        return name + " @ " + address.format();
    }
}

class OrderLine {
    private final String sku;
    private final int qty;
    private final int unitCents;

    OrderLine(String sku, int qty, int unitCents) {
        this.sku = sku;
        this.qty = qty;
        this.unitCents = unitCents;
    }

    int lineTotalCents() {
        return qty * unitCents;
    }

    String describe() {
        return sku + " x" + qty + " =" + lineTotalCents() + " cents";
    }
}

class Order {
    private final String id;
    private final OrderLine[] lines = new OrderLine[8];
    private int count;

    Order(String id) {
        this.id = id;
    }

    void addLine(OrderLine line) {
        if (count >= lines.length) {
            throw new IllegalStateException("order full");
        }
        lines[count++] = line;
    }

    int totalCents() {
        int sum = 0;
        for (int i = 0; i < count; i++) {
            sum += lines[i].lineTotalCents();
        }
        return sum;
    }

    String summary() {
        StringBuilder sb = new StringBuilder(id).append(" total=").append(totalCents()).append(" cents [");
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sb.append("; ");
            }
            sb.append(lines[i].describe());
        }
        return sb.append(']').toString();
    }
}
