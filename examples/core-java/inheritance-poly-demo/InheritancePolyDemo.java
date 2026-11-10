/**
 * Module 05 Ch.03 — inheritance, override, polymorphism intro.
 * javac InheritancePolyDemo.java && java InheritancePolyDemo
 */
public class InheritancePolyDemo {

    public static void main(String[] args) {
        Employee[] team = {
                new Employee("Sam", 50_000),
                new Manager("Riley", 50_000, 10_000),
                new Employee("Jordan", 40_000)
        };

        for (Employee e : team) {
            System.out.println(e.label() + " pay=" + e.annualPay());
        }

        Employee asEmployee = new Manager("Casey", 60_000, 5_000);
        System.out.println("runtime type Manager? " + (asEmployee instanceof Manager));
        System.out.println("polymorphic pay=" + asEmployee.annualPay());
    }
}

class Employee {
    private final String name;
    private final int baseSalary;

    Employee(String name, int baseSalary) {
        this.name = name;
        this.baseSalary = baseSalary;
    }

    String name() {
        return name;
    }

    int baseSalary() {
        return baseSalary;
    }

    int annualPay() {
        return baseSalary;
    }

    String label() {
        return "Employee(" + name + ")";
    }
}

class Manager extends Employee {
    private final int bonus;

    Manager(String name, int baseSalary, int bonus) {
        super(name, baseSalary);
        this.bonus = bonus;
    }

    @Override
    int annualPay() {
        return baseSalary() + bonus;
    }

    @Override
    String label() {
        return "Manager(" + name() + ")";
    }
}
